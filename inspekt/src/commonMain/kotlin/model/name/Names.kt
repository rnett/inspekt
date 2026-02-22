package dev.rnett.inspekt.model.name

import kotlin.js.JsName

/**
 * A fully qualified name of a declaration.
 */
public abstract class QualifiedName {
    /**
     * All segments of the name.
     */
    public abstract fun segments(): List<String>

    /**
     * The name as a Kotlin name string (i.e. using `.` to separate the segments).
     */
    public open fun asString(): String = segments().joinToString(".")
    final override fun toString(): String {
        return asString()
    }
}

/**
 * A package name.
 */
public data class PackageName(val packageNames: List<String>) : QualifiedName() {
    public constructor(vararg packageNames: String) : this(packageNames.toList())

    /**
     * Create a class name for a class located in this package, with [names] class names.
     * @see ClassName
     */
    public fun className(vararg names: String): ClassName = ClassName(this, names.toList())

    /**
     * Create a child package name.
     */
    public fun child(name: String): PackageName = PackageName(packageNames + name)

    /**
     * Create a top-level callable name located in this package, with the [name] simple name.
     */
    public fun member(name: String): CallableName.TopLevel = CallableName.TopLevel(this, name)

    override fun segments(): List<String> = packageNames

    /**
     * True if this is the root package.
     */
    val isRoot: Boolean get() = packageNames.isEmpty()
}

/**
 * The qualified name of a class.
 *
 * For example, a class `B` like
 * ```kotlin
 * package com.example
 * class A { class B {} }
 * ```
 * would have a name like `ClassName(["com", "example"], ["A", "B"])`.
 */
public data class ClassName(
    /**
     * The class's package.
     */
    val packageName: PackageName,
    /**
     * The class's name, and any parent classes.
     * For example, a class `B` like
     * ```kotlin
     * class A { class B {} }
     * ```
     * would have [classNames] `["A", "B"]`.
     */
    val classNames: List<String>
) : QualifiedName() {
    init {
        require(classNames.isNotEmpty()) { "Class names cannot be empty" }
    }

    /**
     * Create a child class name with the [name] simple name.
     */
    public fun child(name: String): ClassName = ClassName(packageName, classNames + name)

    /**
     * Create a member callable name with the [name] simple name.
     */
    public fun member(name: String): CallableName.Member = CallableName.Member(this, name)

    override fun asString(): String {
        return buildString {
            packageName.packageNames.joinTo(this, ".")
            if (!packageName.isRoot) {
                append(".")
            }
            classNames.joinTo(this, ".")
        }
    }

    override fun segments(): List<String> = packageName.segments() + classNames

    /**
     * The simple name of the class.
     * Typically the class's own, non-qualified name.
     */
    val simpleName: String get() = classNames.last()
}

/**
 * The qualified name of a callable declaration, which may either be top level or the member of a class.
 */
public sealed class CallableName : QualifiedName() {
    /**
     * The simple, non-qualified name of the declaration.
     */
    public abstract val name: String

    /**
     * The name of the package that contains this declaration or its parent class.
     */
    public abstract val packageName: PackageName

    /**
     * A top-level callable's qualified name.
     */
    public data class TopLevel(
        /**
         * The declaration's package.
         */
        override val packageName: PackageName,
        /**
         * The declaration's simple name.
         */
        override val name: String
    ) : CallableName() {
        override fun segments(): List<String> = packageName.segments() + name
        override fun asString(): String = buildString {
            packageName.packageNames.joinTo(this, ".")
            if (!packageName.isRoot) {
                append(".")
            }
            append(name)
        }

        override val propertyIfAccessor: TopLevel?
            get() = super.propertyIfAccessor as TopLevel?

        override fun sibling(name: String): TopLevel {
            return TopLevel(packageName, name)
        }
    }

    /**
     * The qualified name of a member of a class.
     */
    public data class Member(
        /**
         * The class this declaration is a member of.
         */
        val className: ClassName,
        /**
         * The declaration's own name.
         */
        override val name: String
    ) : CallableName() {
        /**
         * Whether this name refers to a constructor.
         */
        val isConstructor: Boolean get() = name == SpecialNames.constructor
        override val packageName: PackageName
            get() = className.packageName

        override fun segments(): List<String> = className.segments() + name
        override fun asString(): String = buildString {
            className.packageName.packageNames.joinTo(this, ".")
            if (!className.packageName.isRoot) {
                append(".")
            }
            append(className.classNames.joinToString("."))
            append(".")
            append(name)
        }

        override val propertyIfAccessor: Member?
            get() = super.propertyIfAccessor as Member?

        override fun sibling(name: String): CallableName {
            return Member(className, name)
        }
    }

    /**
     * Create a sibling callable name with the [name] simple name.
     */
    public abstract fun sibling(name: String): CallableName

    /**
     * Whether this is the name of a property accessor.
     */
    public val isPropertyAccessor: Boolean get() = isPropertyGetter || isPropertySetter

    /**
     * Whether this is the name of a property getter.
     */
    public val isPropertyGetter: Boolean get() = SpecialNames.propertyForGetter(name) != null

    /**
     * Whether this is the name of a property setter.
     */
    public val isPropertySetter: Boolean get() = SpecialNames.propertyForSetter(name) != null

    /**
     * Get the corresponding property name if this is the name of a property accessor.
     */
    public open val propertyIfAccessor: CallableName? get() = sibling(SpecialNames.propertyForGetter(name) ?: SpecialNames.propertyForSetter(name) ?: return null)

}

/**
 * Constructs a callable name from the given package names, class names, and simple name.
 * If [classNames] is null, the name is treated as top-level.
 */
public fun CallableName(packageNames: List<String>, classNames: List<String>?, name: String): CallableName {
    if (classNames == null) {
        return CallableName.TopLevel(PackageName(packageNames), name)
    }
    return CallableName.Member(ClassName(PackageName(packageNames), classNames), name)
}

/**
 * Names with special meaning.
 */
public object SpecialNames {
    /**
     * The name of a constructor.
     */
    @JsName("ctor")
    public const val constructor: String = "<init>"

    /**
     * The name of a receiver parameter.
     */
    public const val receiver: String = "this"

    /**
     * The name of a getter for a property named [property].
     */
    public fun getter(property: String): String = "<get-$property>"

    private val getterRegex = Regex("^<get-(.+)>$")

    /**
     * Gets the property name for a getter named [getterName], or `null` if [getterName] is not the name of a getter.
     */
    public fun propertyForGetter(getterName: String): String? = getterRegex.matchEntire(getterName)?.groupValues?.get(1)

    /**
     * The name of a setter for a property named [property].
     */
    public fun setter(property: String): String = "<set-$property>"

    private val setterRegex = Regex("^<set-(.+)>$")

    /**
     * Gets the property name for a setter named [setterName], or `null` if [setterName] is not the name of a setter.
     */
    public fun propertyForSetter(setterName: String): String? = setterRegex.matchEntire(setterName)?.groupValues?.get(1)
}