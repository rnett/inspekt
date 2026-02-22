package dev.rnett.inspekt

import dev.rnett.inspekt.internal.ArgumentsProviderV1
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.jvm.JvmName
import kotlin.reflect.KClass
import kotlin.reflect.KType

/**
 * The parameters of a callable.
 * Parameters are ordered by kind ([Parameter.Kind]):
 *  * Dispatch receiver (if present)
 *  * Any context parameters
 *  * Extension receiver (if present)
 *   * Any value parameters.
 */
public data class Parameters internal constructor(private val parameters: List<Parameter>) : List<Parameter> by parameters {

    private val hasDispatch: Boolean
    private inline val hasExtension: Boolean get() = extensionIndex != null
    private val extensionIndex: Int?
    private val numContext: Int
    private inline val contextStart: Int get() = if (hasDispatch) 1 else 0
    private inline val contextEnd: Int get() = contextStart + numContext
    private inline val valuesStart: Int get() = contextEnd + (if (hasExtension) 1 else 0)
    private val numValues: Int
    private inline val valuesEnd: Int get() = valuesStart + numValues

    init {
        var hasDispatch = false
        var extensionIndex: Int? = null
        var numContext = 0
        var numValues = 0
        parameters.forEachIndexed { index, parameter ->
            when (parameter.kind) {
                Parameter.Kind.DISPATCH -> hasDispatch = true
                Parameter.Kind.CONTEXT -> numContext++
                Parameter.Kind.EXTENSION -> extensionIndex = index
                Parameter.Kind.VALUE -> numValues++
            }
        }
        this.hasDispatch = hasDispatch
        this.extensionIndex = extensionIndex
        this.numValues = numValues
        this.numContext = numContext
    }


    /**
     * Get the parameter named [name], if present.
     * Cannot be used with `"this"`, as it could refer to the dispatch or extension receiver.
     *
     * @throws IllegalArgumentException if [name] is `"this"`
     */
    public operator fun get(name: String): Parameter? {
        if (name == SpecialNames.receiver)
            throw IllegalArgumentException("Cannot get \"this\" by name, as it could refer to the dispatch or extension receiver")
        return find { it.name == name }
    }

    /**
     * Get a parameter by its index in its own kind.
     * For example, `get(Kind.VALUE, 2)` gets the third value parameter.
     *
     * @see value
     * @see context
     * @see dispatch
     * @see extension
     */
    public operator fun get(kind: Parameter.Kind, indexInKind: Int): Parameter {
        return parameters[globalIndex(kind, indexInKind)]
    }

    /**
     * Gets the number of parameters of a given kind.
     */
    public fun count(kind: Parameter.Kind): Int = when (kind) {
        Parameter.Kind.DISPATCH -> if (hasDispatch) 1 else 0
        Parameter.Kind.EXTENSION -> if (hasExtension) 1 else 0
        Parameter.Kind.VALUE -> numValues
        Parameter.Kind.CONTEXT -> numContext
    }

    /**
     * Gets the value parameter at [indexInValueParameters] in the list of value parameters.
     *
     * @throws IndexOutOfBoundsException if [indexInValueParameters] is out of bounds for the value parameters.
     */
    public fun value(indexInValueParameters: Int): Parameter {
        return get(Parameter.Kind.VALUE, indexInValueParameters)
    }

    /**
     * Gets the context parameter at [indexInContextParameters] in the list of context parameters.
     *
     * @throws IndexOutOfBoundsException if [indexInContextParameters] is out of bounds for the context parameters.
     */
    public fun context(indexInContextParameters: Int): Parameter {
        return get(Parameter.Kind.CONTEXT, indexInContextParameters)
    }

    /**
     * Gets the dispatch receiver parameter, if present.
     */
    public val dispatch: Parameter? get() = if (hasDispatch) this[0] else null

    /**
     * Gets the extension receiver parameter, if present.
     */
    public val extension: Parameter? get() = extensionIndex?.let { this[it] }

    /**
     * Gets the value parameters.
     * Does not allocate a new list.
     */
    public val value: List<Parameter> get() = subList(valuesStart, valuesEnd)

    /**
     * Gets the context parameters.
     * Does not allocate a new list.
     */
    public val context: List<Parameter> get() = subList(contextStart, contextEnd)

    /**
     * Get the start offset in the parameter list of the given [kind].
     * Returning a value does not guarantee that parameters of type [kind] are present.
     */
    public fun startOffset(kind: Parameter.Kind): Int = when (kind) {
        Parameter.Kind.DISPATCH -> 0
        Parameter.Kind.CONTEXT -> contextStart
        Parameter.Kind.EXTENSION -> contextEnd
        Parameter.Kind.VALUE -> valuesStart
    }

    /**
     * Gets the index in this list of a parameter at [indexInKind] in its [kind].
     *
     * For example, to get the index in this list of the second value parameter, use `globalIndex(Kind.VALUE, 2)`.
     *
     * Applies bounds and presence checks.
     *
     * @throws IndexOutOfBoundsException if no parameters of [kind] are present, or [indexInKind] is out of bounds for the parameters of [kind].
     */
    public fun globalIndex(kind: Parameter.Kind, indexInKind: Int): Int {
        fun throwError(limit: Int): Nothing {
            throw IndexOutOfBoundsException("Parameter index out of bounds: $indexInKind, number of parameters of kind $kind is $limit")
        }
        return when (kind) {
            Parameter.Kind.DISPATCH -> {
                if (!hasDispatch) {
                    throwError(0)
                }
                if (indexInKind != 0)
                    throwError(1)
                0
            }

            Parameter.Kind.EXTENSION -> {
                if (!hasExtension) {
                    throwError(0)
                }
                if (indexInKind != 0)
                    throwError(1)
                extensionIndex!!
            }

            Parameter.Kind.VALUE -> {
                if (indexInKind !in 0..<numValues)
                    throwError(numValues)
                valuesStart + indexInKind
            }

            Parameter.Kind.CONTEXT -> {
                if (indexInKind !in 0..<numContext)
                    throwError(numContext)
                contextStart + indexInKind
            }
        }
    }

    internal fun defaultIndex(param: Parameter): Int {
        return parameters.subList(0, param.globalIndex).count { it.hasDefault }
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

/**
 * A parameter of a function or other callable declaration.
 */
public data class Parameter internal constructor(
    /**
     * The parameter's type.
     */
    public val type: KType,
    /**
     * Whether the parameter has a default value.
     */
    public val hasDefault: Boolean,
    /**
     * The parameter's name.
     */
    public val name: String,
    public override val annotations: List<Annotation>,
    /**
     * The global index of the parameter in the list of all parameters.
     * @see Parameters
     */
    public val globalIndex: Int,
    /**
     * The index of the parameter in the list of parameters of its [Kind].
     */
    public val indexInKind: Int,
    /**
     * The kind of the parameter: dispatch, context, extension, or value.
     */
    public val kind: Kind
) : AnnotatedElement {
    /**
     * The kind of a parameter.
     */
    public enum class Kind {
        /**
         * A dispatch receiver parameter.
         * For member functions, it is the `this` from the enclosing class.
         * Top-level functions do not have dispatch receivers.
         */
        DISPATCH,

        /**
         * A context parameter, declared with `context(parameter: Type, ...)`
         */
        CONTEXT,

        /**
         * An extension parameter, declared with `fun Type.foo(...)`.
         */
        EXTENSION,

        /**
         * A value parameter, declared with `fun foo(parameter: Type, ...)`
         */
        VALUE
    }

    override fun toString(): String = buildString {
        append(name)
        append(": ")
        append(type.friendlyName)
        if (hasDefault) append(" = ...")
    }

    /**
     * Returns true if the parameter can possibly accept the given value.
     * Returning `true` does not guarantee acceptance (although it typically will be accepted), but returning `false` does guarantee rejection.
     *
     * @see checkValue
     */
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

    /**
     * Throws an exception if the parameter cannot accept the given value.
     * Not throwing does not guarantee acceptance (although it typically will be accepted).
     *
     * @see possiblyAcceptsValue
     */
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

/**
 * A list of argument value assignments for a set of parameters.
 * Guarantees that an argument is provided for all parameters without default values.
 */
public data class ArgumentList internal constructor(
    /**
     * The parameters these arguments correspond to.
     */
    public val parameters: Parameters,
    private val arguments: List<Any?>,
    private val defaultableHasValueBitmask: Int
) : ArgumentsProviderV1, List<Any?> by arguments {

    /**
     * Returns true if the argument for the parameter at [globalIndex] is defaulted (i.e. not set, and the parameter has a default value).
     */
    public fun isDefaulted(globalIndex: Int): Boolean = parameters[globalIndex].let {
        if (it.hasDefault)
            return (defaultableHasValueBitmask and (1 shl parameters.defaultIndex(it))) == 0
        return false
    }

    /**
     * Returns true if the argument for [parameter] is defaulted (i.e. not set, and the parameter has a default value).
     */
    public fun isDefaulted(parameter: Parameter): Boolean = isDefaulted(parameter.globalIndex)

    /**
     * Returns true if the argument for the [kind] parameter at [indexInKind] is defaulted (i.e. not set, and the parameter has a default value).
     */
    public fun isDefaulted(kind: Parameter.Kind, indexInKind: Int): Boolean = isDefaulted(parameters[kind, indexInKind])

    /**
     * Get the argument for the parameter at [index], or null if one was not set.
     * To distinguish between explicitly passing `null` and using the default value, use [isDefaulted].
     *
     * @see isDefaulted
     */
    public override operator fun get(index: Int): Any? = arguments[index]

    /**
     * Get the argument for the parameter with the given [name], or null if one was not set.
     * To distinguish between explicitly passing `null` and using the default value, use [isDefaulted].
     *
     * @see isDefaulted
     *
     * @throws IllegalArgumentException if [name] == `"this"`, because the parameter would be ambiguous.
     */
    public operator fun get(name: String): Any? = parameters[name]?.let { get(it.globalIndex) }

    /**
     * Get the argument for [parameter], or null if one was not set.
     * To distinguish between explicitly passing `null` and using the default value, use [isDefaulted].
     *
     * @see isDefaulted
     */
    public operator fun get(parameter: Parameter): Any? = arguments[parameter.globalIndex]

    /**
     * Get the argument for the [kind] parameter at [indexInKind], or null if one was not set.
     * To distinguish between explicitly passing `null` and using the default value, use [isDefaulted].
     *
     * @see isDefaulted
     */
    public operator fun get(kind: Parameter.Kind, indexInKind: Int): Any? {
        return get(parameters[kind, indexInKind])
    }

    /**
     * Gets the dispatch receiver if one is set.
     */
    public val dispatchReceiver: Any? = if (parameters.dispatch != null) arguments[0] else null

    /**
     * Gets the extension receiver if one is set.
     */
    public val extensionReceiver: Any? = parameters.extension?.let { arguments[it.globalIndex] }

    /**
     * Gets the contex parameter at [index], if it is set.
     * To distinguish between explicitly passing `null` and using the default value, use [isDefaulted].
     *
     * @see isDefaulted
     * @throws IndexOutOfBoundsException if [index] is out of bounds for the context parameters.
     */
    public fun context(index: Int): Any? = get(parameters.globalIndex(Parameter.Kind.CONTEXT, index))

    /**
     * Gets the value parameter at [index], if it is set.
     * To distinguish between explicitly passing `null` and using the default value, use [isDefaulted].
     *
     * @see isDefaulted
     * @throws IndexOutOfBoundsException if [index] is out of bounds for the value parameters.
     */
    public fun value(index: Int): Any? = get(parameters.globalIndex(Parameter.Kind.VALUE, index))

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

    /**
     * A builder to assemble an [ArgumentList] for a set of parameters.
     * Parameters which are not set will use their default values if they have one.
     * Successfully building an [ArgumentList] guarantees that values are provided for all parameters without default values.
     */
    public class Builder @PublishedApi internal constructor(private val parameters: Parameters) {
        init {
            if (parameters.count { it.hasDefault } > 32) {
                throw IllegalArgumentException("Can not call function with more than 32 default arguments")
            }
        }

        // bit of (# defaultable param) is 1 if the param was set
        private var defaultableHasValueBitmask: Int = 0
        private val args = arrayOfNulls<Any?>(parameters.size)

        /**
         * Sets the argument value for the parameter at index [globalIndex].
         *
         * @throws IndexOutOfBoundsException if [globalIndex] is out of bounds for these parameters.
         */
        public operator fun set(globalIndex: Int, value: Any?) {
            if (globalIndex >= parameters.size) throw IndexOutOfBoundsException("Argument index out of bounds: $globalIndex, number of parameters is ${parameters.size}")
            val param = parameters[globalIndex]
            if (param.hasDefault) {
                defaultableHasValueBitmask = defaultableHasValueBitmask or (1 shl parameters.defaultIndex(param))
            }
            args[globalIndex] = value
        }

        /**
         * Sets multiple argument values based on [args].
         * For each item in [args], sets the argument value for the parameter at index `[idx + offset]`.
         *
         * @throws IndexOutOfBoundsException if any index is out of bounds for these parameters.
         */
        public fun setAll(args: Iterable<Any?>, offset: Int = 0) {
            args.forEachIndexed { idx, it ->
                this[idx + offset] = it
            }
        }

        /**
         * Sets multiple argument values based on [args].
         * For each item in [args], sets the argument value for the parameter at index `[idx + offset]`.
         *
         * @throws IndexOutOfBoundsException if any index is out of bounds for these parameters.
         */
        @JvmName("setAllArray")
        public fun setAll(args: Array<Any?>, offset: Int = 0) {
            args.forEachIndexed { idx, it ->
                this[idx + offset] = it
            }
        }

        /**
         * Sets multiple argument values based on [args].
         * For each item in [args], sets the argument value for the parameter at index `[idx + offset]`.
         *
         * @throws IndexOutOfBoundsException if any index is out of bounds for these parameters.
         */
        @JvmName("setAllVararg")
        public fun setAll(vararg args: Any?, offset: Int = 0) {
            args.forEachIndexed { idx, it ->
                this[idx + offset] = it
            }
        }

        /**
         * Sets multiple argument values for [kind] parameters based on [args].
         * For each item in [args], sets the argument value for the parameter at index `[kindOffset(kind) + idx + offset]`.
         *
         * @throws IndexOutOfBoundsException if any index is out of bounds for these parameters.
         */
        public fun setAll(kind: Parameter.Kind, args: Iterable<Any?>, offset: Int = 0) {
            args.forEachIndexed { idx, it ->
                this[kind, idx + offset] = it
            }
        }

        /**
         * Sets multiple argument values for [kind] parameters based on [args].
         * For each item in [args], sets the argument value for the parameter at index `[kindOffset(kind) + idx + offset]`.
         *
         * @throws IndexOutOfBoundsException if any index is out of bounds for these parameters.
         */
        @JvmName("setAllArray")
        public fun setAll(kind: Parameter.Kind, args: Array<Any?>, offset: Int = 0) {
            args.forEachIndexed { idx, it ->
                this[kind, idx + offset] = it
            }
        }

        /**
         * Sets multiple argument values for [kind] parameters based on [args].
         * For each item in [args], sets the argument value for the parameter at index `[kindOffset(kind) + idx + offset]`.
         *
         * @throws IndexOutOfBoundsException if any index is out of bounds for these parameters.
         */
        @JvmName("setAllVararg")
        public fun setAll(kind: Parameter.Kind, vararg args: Any?, offset: Int = 0) {
            args.forEachIndexed { idx, it ->
                this[kind, idx + offset] = it
            }
        }

        /**
         * Setter for the dispatch receiver parameter.
         * Getting always returns null.
         */
        public var dispatchReceiver: Any?
            get() = null
            set(value) {
                set(Parameter.Kind.DISPATCH, 0, value)
            }

        /**
         * Setter for the dispatch receiver parameter.
         * Getting always returns null.
         */
        public var extensionReceiver: Any?
            get() = null
            set(value) {
                set(Parameter.Kind.EXTENSION, 0, value)
            }

        /**
         * Sets the argument value for the [kind] parameter at [indexInKind].
         *
         * @throws IndexOutOfBoundsException if [indexInKind] is out of bounds for parameters of kind [kind].
         */
        public operator fun set(kind: Parameter.Kind, indexInKind: Int, value: Any?) {
            val globalIndex = parameters.globalIndex(kind, indexInKind)
            this[globalIndex] = value
        }

        /**
         * Sets the value of the parameter with the given [name].
         *
         * @throws IllegalArgumentException if no parameter with the given [name] is found, or if [name] == `"this"`..
         */
        public operator fun set(name: String, value: Any?) {
            val param = parameters[name] ?: throw IllegalArgumentException("Parameter with name \"$name\" not found")
            this[param] = value
        }

        /**
         * Sets the value of the given parameter.
         *
         * @throws IllegalArgumentException if [parameters] is not in the parameter set for this builder.
         */
        public operator fun set(parameter: Parameter, value: Any?) {
            if (parameter.globalIndex < 0 || parameter.globalIndex >= parameters.size || parameters[parameter.globalIndex] != parameter) {
                throw IllegalArgumentException("Parameter $parameter is not in the parameter set for this builder")
            }
            set(parameter.globalIndex, value)
        }

        /**
         * Set multiple arguments based on the parameter names.
         *
         * @throws IllegalArgumentException if no parameter with the given name is found, or if name == `"this"`..
         */
        public fun set(vararg values: Pair<String, Any?>) {
            values.forEach { this[it.first] = it.second }
        }


        /**
         * Sets multiple argument values for context parameters based on [values].
         * For each item in [values], sets the argument value for the parameter at index `[contextParameterStart + idx + offset]`.
         *
         * @throws IndexOutOfBoundsException if any index is out of bounds for these parameters.
         */
        public fun context(vararg values: Any?, offset: Int = 0) {
            setAll(Parameter.Kind.CONTEXT, *values, offset = offset)
        }


        /**
         * Sets multiple argument values for value parameters based on [values].
         * For each item in [values], sets the argument value for the parameter at index `[valueParameterStart + idx + offset]`.
         *
         * @throws IndexOutOfBoundsException if any index is out of bounds for these parameters.
         */
        public fun value(vararg values: Any?, offset: Int = 0) {
            setAll(Parameter.Kind.VALUE, *values, offset = offset)
        }

        @PublishedApi
        internal fun build(): ArgumentList = ArgumentList(parameters, args.asList(), defaultableHasValueBitmask)
    }
}

/**
 * Builds an [ArgumentList] for a set of parameters.
 * Parameters which are not set will use their default values if they have one.
 * Successfully building an [ArgumentList] guarantees that values are provided for all parameters without default values.
 */
public inline fun ArgumentList(parameters: Parameters, builder: ArgumentsBuilder): ArgumentList {
    contract {
        callsInPlace(builder, InvocationKind.EXACTLY_ONCE)
    }
    return ArgumentList.Builder(parameters).apply {
        builder(parameters)
    }.build()
}

/**
 * Builds an [ArgumentList] for a set of parameters.
 * Parameters which are not set will use their default values if they have one.
 * Successfully building an [ArgumentList] guarantees that values are provided for all parameters without default values.
 */
public inline fun Callable.buildArguments(builder: ArgumentsBuilder): ArgumentList {
    contract {
        callsInPlace(builder, InvocationKind.EXACTLY_ONCE)
    }
    return ArgumentList.Builder(parameters).apply {
        builder(parameters)
    }.build()
}

/**
 * Builds an [ArgumentList] for a set of parameters, where all argument values are known.
 * The size of [args] must exactly match the size of [parameters].
 */
public fun ArgumentList(parameters: Parameters, args: List<Any?>): ArgumentList {
    if (args.size != parameters.size) {
        throw IllegalArgumentException("Creating ArgumentList from list requires specifying all ${parameters.size} arguments, received ${args.size}")
    }
    return ArgumentList(parameters, args, 1)
}

/**
 * A builder of an [ArgumentsBuilder].
 */
public typealias ArgumentsBuilder = ArgumentList.Builder.(Parameters) -> Unit