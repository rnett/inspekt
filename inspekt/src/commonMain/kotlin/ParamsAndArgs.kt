package dev.rnett.inspekt

import dev.rnett.inspekt.internal.ArgumentsProviderV1
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.jvm.JvmName
import kotlin.reflect.KClass
import kotlin.reflect.KType


public data class Parameters internal constructor(private val parameters: List<Parameter>) : List<Parameter> by parameters {
    public operator fun get(name: String): Parameter? {
        if (name == SpecialNames.receiver)
            throw IllegalArgumentException("Cannot get \"this\" by name, as it could refer to the dispatch or extension receiver")
        return find { it.name == name }
    }

    public operator fun get(kind: Parameter.Kind, indexInKind: Int): Parameter {
        val kindCount = count(kind)
        if (indexInKind >= kindCount)
            throw IllegalArgumentException("Parameter index out of bounds: $indexInKind, number of parameters of kind $kind is $kindCount")
        return parameters[startOffset(kind) + indexInKind]
    }

    public fun count(kind: Parameter.Kind): Int = parameters.count { it.kind == kind }

    public val dispatch: Parameter? get() = find { it.kind == Parameter.Kind.DISPATCH }
    public val extension: Parameter? get() = find { it.kind == Parameter.Kind.EXTENSION }

    public val value: List<Parameter> get() = filter { it.kind == Parameter.Kind.VALUE }
    public val context: List<Parameter> get() = filter { it.kind == Parameter.Kind.CONTEXT }

    public fun startOffset(kind: Parameter.Kind): Int = count { it.kind < kind }
    public fun globalIndex(kind: Parameter.Kind, indexInKind: Int): Int {
        val kindCount = count(kind)
        if (indexInKind >= kindCount)
            throw IllegalArgumentException("Parameter index out of bounds: $indexInKind, number of parameters of kind $kind is $kindCount")

        return startOffset(kind) + indexInKind
    }

    internal fun defaultIndex(param: Parameter): Int {
        val index = parameters.indexOf(param).takeUnless { it == -1 } ?: error("Not in params list")
        return parameters.subList(0, index).count { it.hasDefault }
    }

    override fun toString(): String = buildString {
        append("(")
        joinTo(this, ", ")
        append(")")
    }

    public inline fun buildArguments(builder: ArgumentsBuilder): ArgumentList {
        contract { callsInPlace(builder, InvocationKind.EXACTLY_ONCE) }
        return ArgumentList(this, builder)
    }

    /**
     * Requires specifying a value for all parameters, aka `args.size == this.size`.
     */
    public fun arguments(args: Iterable<Any?>): ArgumentList {
        return ArgumentList(this, args.toList())
    }

    /**
     * Requires specifying a value for all parameters, aka `args.size == this.size`.
     */
    public fun arguments(args: Array<Any?>): ArgumentList {
        return ArgumentList(this, args.asList())
    }
}

public data class Parameter internal constructor(
    public val type: KType,
    public val hasDefault: Boolean,
    public val name: String,
    public override val annotations: List<Annotation>,
    public val globalIndex: Int,
    public val indexInKind: Int,
    public val kind: Kind
) : AnnotatedElement {
    public enum class Kind {
        DISPATCH,
        CONTEXT,
        EXTENSION,
        VALUE
    }

    override fun toString(): String = buildString {
        append(name)
        append(": ")
        append(type.friendlyName)
        if (hasDefault) append(" = ...")
    }

    public fun possiblyAcceptsValue(value: Any?): Boolean {
        if (value == null && !type.isMarkedNullable)
            return false

        if (value != null) {
            val kClass = type.classifier as? KClass<*>
            if (kClass != null) {
                if (!kClass.isInstance(value))
                    return false
            }
        }
        return true
    }

    public fun checkValue(value: Any?) {
        if (value == null && !type.isMarkedNullable)
            throw IllegalArgumentException("Argument for parameter $this was null, but the parameter is not nullable")

        if (value != null) {
            val kClass = type.classifier as? KClass<*>
            if (kClass != null) {
                if (!kClass.isInstance(value))
                    throw IllegalArgumentException("Argument for parameter $this was not an instance of $type")
            }
        }
    }
}

public data class ArgumentList internal constructor(private val parameters: Parameters, private val arguments: Array<Any?>, private val defaultableHasValueBitmask: Int) : ArgumentsProviderV1 {
    public fun isDefaulted(globalIndex: Int): Boolean = parameters[globalIndex].let {
        if (it.hasDefault)
            return (defaultableHasValueBitmask and (1 shl parameters.defaultIndex(it))) == 0
        return false
    }

    public operator fun get(globalIndex: Int): Any? = arguments[globalIndex]
    public operator fun get(name: String): Any? = parameters[name]?.let { get(it.globalIndex) }

    override fun v1Get(globalIndex: Int): Any? = get(globalIndex)

    override val v1DefaultableHasValueBitmask: Int
        get() = defaultableHasValueBitmask

    init {
        parameters.forEach {
            if (!it.hasDefault && isDefaulted(it.globalIndex))
                throw IllegalArgumentException("Parameter [${it.kind}] ${it.name}: ${it.type} with index ${it.globalIndex} has no default value and is not provided")

            if (!isDefaulted(it.globalIndex)) {
                val arg = arguments[it.globalIndex]
                it.checkValue(arg)
            }
        }
    }

    public class Builder @PublishedApi internal constructor(private val parameters: Parameters) {
        init {
            if (parameters.count { it.hasDefault } > 32) {
                throw IllegalArgumentException("Can not call function with more than 32 default arguments")
            }
        }

        // bit of (# defaultable param) is 1 if the param was set
        private var defaultableHasValueBitmask: Int = 0
        private val args = arrayOfNulls<Any?>(parameters.size)

        public operator fun set(index: Int, value: Any?) {
            if (index >= parameters.size) throw IllegalArgumentException("Argument index out of bounds: $index, number of parameters is ${parameters.size}")
            val param = parameters[index]
            if (param.hasDefault) {
                defaultableHasValueBitmask = defaultableHasValueBitmask or (1 shl parameters.defaultIndex(param))
            }
            args[index] = value
        }

        public fun setAll(args: Iterable<Any?>, offset: Int = 0) {
            args.forEachIndexed { idx, it ->
                this[idx + offset] = it
            }
        }

        public fun setAll(args: Array<Any?>, offset: Int = 0) {
            args.forEachIndexed { idx, it ->
                this[idx + offset] = it
            }
        }

        @JvmName("setAllVararg")
        public fun setAll(vararg args: Any?, offset: Int = 0) {
            args.forEachIndexed { idx, it ->
                this[idx + offset] = it
            }
        }

        public fun setAll(kind: Parameter.Kind, args: Iterable<Any?>, offset: Int = 0) {
            args.forEachIndexed { idx, it ->
                this[kind, idx + offset] = it
            }
        }

        public fun setAll(kind: Parameter.Kind, args: Array<Any?>, offset: Int = 0) {
            args.forEachIndexed { idx, it ->
                this[kind, idx + offset] = it
            }
        }

        @JvmName("setAllVararg")
        public fun setAll(kind: Parameter.Kind, vararg args: Any?, offset: Int = 0) {
            args.forEachIndexed { idx, it ->
                this[kind, idx + offset] = it
            }
        }

        public var dispatchReceiver: Any?
            get() = null
            set(value) {
                set(Parameter.Kind.DISPATCH, 0, value)
            }

        public var extensionReceiver: Any?
            get() = null
            set(value) {
                set(Parameter.Kind.EXTENSION, 0, value)
            }

        public operator fun set(kind: Parameter.Kind, indexInKind: Int, value: Any?) {
            val globalIndex = parameters.globalIndex(kind, indexInKind)
            this[globalIndex] = value
        }

        public operator fun set(name: String, value: Any?) {
            val param = parameters[name] ?: throw IllegalArgumentException("Parameter with name \"$name\" not found")
            this[param] = value
        }

        public operator fun set(parameter: Parameter, value: Any?) {
            set(parameter.globalIndex, value)
        }

        public fun set(vararg values: Pair<String, Any?>) {
            values.forEach { this[it.first] = it.second }
        }

        public fun context(vararg values: Any?, offset: Int = 0) {
            setAll(Parameter.Kind.CONTEXT, *values, offset = offset)
        }

        public fun value(vararg values: Any?, offset: Int = 0) {
            setAll(Parameter.Kind.VALUE, *values, offset = offset)
        }

        @PublishedApi
        internal fun build(): ArgumentList = ArgumentList(parameters, args, defaultableHasValueBitmask)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ArgumentList) return false

        if (parameters != other.parameters) return false
        if (!arguments.contentEquals(other.arguments)) return false
        if (defaultableHasValueBitmask != other.defaultableHasValueBitmask) return false

        return true
    }

    override fun hashCode(): Int {
        var result = parameters.hashCode()
        result = 31 * result + arguments.contentHashCode()
        result = 31 * result + defaultableHasValueBitmask.hashCode()
        return result
    }
}

public inline fun ArgumentList(parameters: Parameters, builder: ArgumentsBuilder): ArgumentList {
    contract {
        callsInPlace(builder, InvocationKind.EXACTLY_ONCE)
    }
    return ArgumentList.Builder(parameters).apply {
        builder(parameters)
    }.build()
}

public fun ArgumentList(parameters: Parameters, args: List<Any?>): ArgumentList {
    if (args.size != parameters.size) {
        throw IllegalArgumentException("Creating ArgumentList from list requires specifying all ${parameters.size} arguments, received ${args.size}")
    }
    return ArgumentList(parameters) {
        args.forEachIndexed { idx, it ->
            this[idx] = it
        }
    }
}

public typealias ArgumentsBuilder = ArgumentList.Builder.(Parameters) -> Unit