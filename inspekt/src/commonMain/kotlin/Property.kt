package dev.rnett.inspekt

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.KType

/**
 * The result of inspekting a property.
 */
public sealed class Property : Callable() {
    abstract override val kotlin: KProperty1<*, *>

    /**
     * Whether the property has a backing field.
     */
    public abstract val hasBackingField: Boolean

    /**
     * Whether the property has a delegate.
     */
    public abstract val hasDelegate: Boolean

    /**
     * The type of the property.
     */
    public abstract val type: KType

    /**
     * The property's getter.
     */
    public abstract val getter: PropertyGetter

    /**
     * The property's setter, if it has one.
     */
    public open val setter: PropertySetter? get() = null

    protected fun StringBuilder.appendPropertySignature(keyword: String, includeFullName: Boolean) {
        appendContext()
        if (isAbstract) append("abstract ")
        append(keyword).append(" ")
        appendExtension()

        if (includeFullName) {
            append(name)
        } else {
            append(shortName)
        }
        append(": ")
        append(type.friendlyName)

        if (inheritedFrom != null)
            append(" @ ${inheritedFrom!!.friendlyName}")
    }

    /**
     * Get the value of a property by calling its getter.
     *
     * @throws FunctionInvocationException if invocation fails
     */
    public fun get(arguments: ArgumentsBuilder): Any? = getter.invoke(arguments)

    /**
     * Gets the value of a property by calling its getter.
     *
     * @throws FunctionInvocationException if invocation fails
     */
    public fun get(arguments: ArgumentList): Any? = getter.invoke(arguments)
}

/**
 * The result of inspekting a `val` property.
 */
public data class ReadOnlyProperty internal constructor(
    override val kotlin: KProperty1<*, *>,
    override val hasBackingField: Boolean,
    override val hasDelegate: Boolean,
    override val type: KType,
    override val getter: PropertyGetter,
    override val name: CallableName,
    override val parameters: Parameters,
    override val annotations: List<Annotation>,
    override val isAbstract: Boolean,
    override val inheritedFrom: KClass<*>?,
) : Property() {

    override fun toString(includeFullName: Boolean): String = buildString {
        appendPropertySignature("val", includeFullName)
    }
}

/**
 * The result of inspekting a `var` property.
 */
public data class MutableProperty internal constructor(
    override val kotlin: KMutableProperty1<*, *>,
    override val hasBackingField: Boolean,
    override val hasDelegate: Boolean,
    override val type: KType,
    override val getter: PropertyGetter,
    override val setter: PropertySetter,
    override val name: CallableName,
    override val parameters: Parameters,
    override val annotations: List<Annotation>,
    override val isAbstract: Boolean,
    override val inheritedFrom: KClass<*>?
) : Property() {
    /**
     * Set the value of a property by calling its setter.
     *
     * @throws FunctionInvocationException if invocation fails
     */
    public fun set(arguments: ArgumentsBuilder): Any? = setter.invoke(arguments)

    /**
     * Set the value of a property by calling its setter.
     *
     * @throws FunctionInvocationException if invocation fails
     */
    public fun set(arguments: ArgumentList): Any? = setter.invoke(arguments)

    override fun toString(includeFullName: Boolean): String = buildString {
        appendPropertySignature("var", includeFullName)
    }
}