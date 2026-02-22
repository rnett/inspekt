package dev.rnett.inspekt.model

import dev.rnett.inspekt.model.arguments.ArgumentList
import dev.rnett.inspekt.model.arguments.ArgumentsBuilder
import dev.rnett.inspekt.model.name.CallableName
import dev.rnett.inspekt.utils.friendlyName
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.KType

/**
 * The result of inspekting a property.
 */
public sealed class Property(
    parameters: Parameters,
    isAbstract: Boolean,
    inheritedFrom: KClass<*>?,
    annotations: List<Annotation>,
    /**
     * Whether the property has a backing field.
     */
    public val hasBackingField: Boolean,
    /**
     * Whether the property has a delegate.
     */
    public val hasDelegate: Boolean,
    /**
     * The type of the property.
     */
    public val type: KType,
    /**
     * The property's getter.
     */
    public val getter: PropertyGetter,
    override val name: CallableName
) : Callable(parameters, isAbstract, inheritedFrom, annotations) {
    abstract override val kotlin: KProperty1<*, *>


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
            append(" @ ${inheritedFrom.friendlyName}")
    }

    /**
     * Get the value of a property by calling its getter.
     *
     * @throws dev.rnett.inspekt.exceptions.FunctionInvocationException if invocation fails
     */
    public fun get(arguments: ArgumentsBuilder): Any? = getter.invoke(arguments)

    /**
     * Gets the value of a property by calling its getter.
     *
     * @throws dev.rnett.inspekt.exceptions.FunctionInvocationException if invocation fails
     */
    public fun get(arguments: ArgumentList): Any? = getter.invoke(arguments)
}

/**
 * The result of inspekting a `val` property.
 */
public class ReadOnlyProperty internal constructor(
    override val kotlin: KProperty1<*, *>,
    hasBackingField: Boolean,
    hasDelegate: Boolean,
    type: KType,
    getter: PropertyGetter,
    name: CallableName,
    parameters: Parameters,
    annotations: List<Annotation>,
    isAbstract: Boolean,
    inheritedFrom: KClass<*>?,
) : Property(parameters, isAbstract, inheritedFrom, annotations, hasBackingField, hasDelegate, type, getter, name) {

    override fun toString(includeFullName: Boolean): String = buildString {
        appendPropertySignature("val", includeFullName)
    }
}

/**
 * The result of inspekting a `var` property.
 */
public class MutableProperty internal constructor(
    override val kotlin: KMutableProperty1<*, *>,
    hasBackingField: Boolean,
    hasDelegate: Boolean,
    type: KType,
    getter: PropertyGetter,
    override val setter: PropertySetter,
    name: CallableName,
    parameters: Parameters,
    annotations: List<Annotation>,
    isAbstract: Boolean,
    inheritedFrom: KClass<*>?
) : Property(parameters, isAbstract, inheritedFrom, annotations, hasBackingField, hasDelegate, type, getter, name) {
    /**
     * Set the value of a property by calling its setter.
     *
     * @throws dev.rnett.inspekt.exceptions.FunctionInvocationException if invocation fails
     */
    public fun set(arguments: ArgumentsBuilder): Any? = setter.invoke(arguments)

    /**
     * Set the value of a property by calling its setter.
     *
     * @throws dev.rnett.inspekt.exceptions.FunctionInvocationException if invocation fails
     */
    public fun set(arguments: ArgumentList): Any? = setter.invoke(arguments)

    override fun toString(includeFullName: Boolean): String = buildString {
        appendPropertySignature("var", includeFullName)
    }
}