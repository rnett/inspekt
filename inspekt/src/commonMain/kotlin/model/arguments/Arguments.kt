package dev.rnett.inspekt.model.arguments

import dev.rnett.inspekt.internal.ArgumentsProviderV1
import dev.rnett.inspekt.model.Callable
import dev.rnett.inspekt.model.Parameter
import dev.rnett.inspekt.model.Parameters
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.jvm.JvmName


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
            if (!it.hasDefault && isDefaulted(it.globalIndex)) {
                when (it.kind) {
                    Parameter.Kind.DISPATCH -> throw IllegalArgumentException("Argument for the dispatch receiver not set.")
                    Parameter.Kind.EXTENSION -> throw IllegalArgumentException("Argument for the extension receiver not set.")
                    else -> throw IllegalArgumentException("${it.kind.humanName} ${it.name}: ${it.type} with index ${it.globalIndex} has no default value and is not provided")
                }
            }

            if (!isDefaulted(it.globalIndex)) {
                val arg = arguments[it.globalIndex]
                it.checkValue(arg)
            }
        }
    }

    override fun toString(): String {
        return parameters.zip(arguments).joinToString(prefix = "(", postfix = ")", separator = ", ") { (param, arg) ->
            param.name + " = " + if (isDefaulted(param)) "<default>" else arg
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
 * A builder lambda for a [ArgumentList.Builder].
 */
public typealias ArgumentsBuilder = ArgumentList.Builder.(Parameters) -> Unit