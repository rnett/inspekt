package dev.rnett.spekt

import dev.rnett.spekt.internal.ArgumentsProviderV1
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.reflect.KClass
import kotlin.reflect.KType


public data class Parameters internal constructor(private val parameters: List<Parameter>) : List<Parameter> by parameters {
    public operator fun get(name: String): Parameter? {
        if (name == SpecialNames.receiver)
            throw IllegalArgumentException("Cannot get \"this\" by name, as it could refer to the dispatch or extension receiver")
        return find { it.name == name }
    }

    public val dispatch: Parameter? get() = find { it.kind == Parameter.Kind.DISPATCH }
    public val extension: Parameter? get() = find { it.kind == Parameter.Kind.EXTENSION }

    public val value: List<Parameter> get() = filter { it.kind == Parameter.Kind.VALUE }
    public val context: List<Parameter> get() = filter { it.kind == Parameter.Kind.CONTEXT }

    public fun startOffset(kind: Parameter.Kind): Int = count { it.kind < kind }
    public fun globalIndex(kind: Parameter.Kind, indexInKind: Int): Int = startOffset(kind) + indexInKind

    override fun toString(): String = buildString {
        append("(")
        joinTo(this, ", ")
        append(")")
    }

    public inline fun buildArguments(builder: ArgumentsBuilder): ArgumentList {
        contract { callsInPlace(builder, InvocationKind.EXACTLY_ONCE) }
        return ArgumentList(this, builder)
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

public data class ArgumentList internal constructor(private val parameters: Parameters, private val arguments: Array<Any?>, private val hasBeenSet: BitSet) : ArgumentsProviderV1 {
    public fun isDefaulted(globalIndex: Int): Boolean = !hasBeenSet[globalIndex]
    public operator fun get(globalIndex: Int): Any? = arguments[globalIndex]
    public operator fun get(name: String): Any? = parameters[name]?.let { get(it.globalIndex) }

    override fun v1Get(globalIndex: Int): Any? = get(globalIndex)

    override fun v1IsDefaulted(globalIndex: Int): Boolean = isDefaulted(globalIndex)

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
        private val hasBeenSet = BitSet(parameters.size)
        private val args = arrayOfNulls<Any?>(parameters.size)

        public operator fun set(index: Int, value: Any?) {
            hasBeenSet[index] = true
            args[index] = value
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
            this[parameters.globalIndex(kind, indexInKind)] = value
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

        public fun context(vararg values: Any?) {
            values.forEachIndexed { index, value ->
                this[Parameter.Kind.CONTEXT, index] = value
            }
        }

        public fun value(vararg values: Any?) {
            values.forEachIndexed { index, value ->
                this[Parameter.Kind.VALUE, index] = value
            }
        }

        @PublishedApi
        internal fun build(): ArgumentList = ArgumentList(parameters, args, hasBeenSet)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ArgumentList) return false

        if (parameters != other.parameters) return false
        if (!arguments.contentEquals(other.arguments)) return false
        if (hasBeenSet != other.hasBeenSet) return false

        return true
    }

    override fun hashCode(): Int {
        var result = parameters.hashCode()
        result = 31 * result + arguments.contentHashCode()
        result = 31 * result + hasBeenSet.hashCode()
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

public typealias ArgumentsBuilder = ArgumentList.Builder.(Parameters) -> Unit