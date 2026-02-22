package dev.rnett.inspekt.model

import dev.rnett.inspekt.model.arguments.ArgumentList
import dev.rnett.inspekt.model.arguments.ArgumentsBuilder
import dev.rnett.inspekt.model.name.SpecialNames
import dev.rnett.inspekt.utils.friendlyName
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
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

    val hasDispatch: Boolean
    val hasExtension: Boolean get() = extensionIndex != null
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
    public enum class Kind(public val humanName: String) {
        /**
         * A dispatch receiver parameter.
         * For member functions, it is the `this` from the enclosing class.
         * Top-level functions do not have dispatch receivers.
         */
        DISPATCH("Dispatch receiver"),

        /**
         * A context parameter, declared with `context(parameter: Type, ...)`
         */
        CONTEXT("Context parameter"),

        /**
         * An extension parameter, declared with `fun Type.foo(...)`.
         */
        EXTENSION("Extension receiver"),

        /**
         * A value parameter, declared with `fun foo(parameter: Type, ...)`
         */
        VALUE("Value parameter")
    }

    override fun toString(): String = buildString {
        if (kind == Kind.DISPATCH) {
            append("this@dispatch")
        } else if (kind == Kind.EXTENSION) {
            append("this@extension")
        } else {
            append(name)
        }
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
        if (value == null && !type.isMarkedNullable) {
            if (this.kind == Kind.DISPATCH) {
                throw IllegalArgumentException("Argument for the dispatch receiver was null. This is not allowed.")
            }
            throw IllegalArgumentException("Argument for parameter $this was null, but the parameter is not nullable")
        }

        if (value != null) {
            val kClass = type.classifier as? KClass<*>
            if (kClass != null) {
                if (!kClass.isInstance(value))
                    throw IllegalArgumentException("Argument for parameter $this was not an instance of $type")
            }
        }
    }
}
