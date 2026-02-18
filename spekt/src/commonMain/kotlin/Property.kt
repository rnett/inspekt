package dev.rnett.spekt

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.KType

public sealed class Property : Callable() {
    abstract override val kotlin: KProperty1<*, *>
    public abstract val isConstructorProperty: Boolean
    public abstract val hasBackingField: Boolean
    public abstract val hasDelegate: Boolean

    public abstract val type: KType

    public abstract val getter: PropertyMethod

    protected fun StringBuilder.appendPropertySignature(keyword: String, includeFullName: Boolean) {
        if (isConstructorProperty) {
            append("[constructor] ")
        }
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

    public fun get(arguments: ArgumentsBuilder): Any? = getter.invoke(arguments)
    public fun get(arguments: ArgumentList): Any? = getter.invoke(arguments)
}

public data class ReadOnlyProperty internal constructor(
    override val kotlin: KProperty1<*, *>,
    override val isConstructorProperty: Boolean,
    override val hasBackingField: Boolean,
    override val hasDelegate: Boolean,
    override val type: KType,
    override val getter: PropertyMethod,
    override val name: MemberName,
    override val parameters: Parameters,
    override val annotations: List<Annotation>,
    override val isAbstract: Boolean,
    override val inheritedFrom: KClass<*>?,
) : Property() {

    override fun toString(includeFullName: Boolean): String = buildString {
        appendPropertySignature("val", includeFullName)
    }
}

public data class MutableProperty internal constructor(
    override val kotlin: KMutableProperty1<*, *>,
    override val isConstructorProperty: Boolean,
    override val hasBackingField: Boolean,
    override val hasDelegate: Boolean,
    override val type: KType,
    override val getter: PropertyMethod,
    public val setter: PropertyMethod,
    override val name: MemberName,
    override val parameters: Parameters,
    override val annotations: List<Annotation>,
    override val isAbstract: Boolean,
    override val inheritedFrom: KClass<*>?
) : Property() {
    public fun set(arguments: ArgumentsBuilder): Any? = setter.invoke(arguments)
    public fun set(arguments: ArgumentList): Any? = setter.invoke(arguments)

    override fun toString(includeFullName: Boolean): String = buildString {
        appendPropertySignature("var", includeFullName)
    }
}