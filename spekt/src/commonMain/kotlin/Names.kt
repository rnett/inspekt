package dev.rnett.spekt

import kotlin.js.JsName

public abstract class QualifiedName {
    public abstract fun segments(): List<String>
    public open fun asString(): String = segments().joinToString(".")
    override fun toString(): String {
        return asString()
    }
}

public data class PackageName(val packageNames: List<String>) : QualifiedName() {
    public constructor(vararg packageNames: String) : this(packageNames.toList())

    public fun className(vararg names: String): ClassName = ClassName(this, names.toList())
    public fun child(name: String): PackageName = PackageName(packageNames + name)
    public fun member(name: String): MemberName.TopLevel = MemberName.TopLevel(this, name)

    override fun segments(): List<String> = packageNames

    val isRoot: Boolean get() = packageNames.isEmpty()
}

public data class ClassName(val packageName: PackageName, val classNames: List<String>) : QualifiedName() {
    init {
        require(classNames.isNotEmpty()) { "Class names cannot be empty" }
    }

    public fun child(name: String): ClassName = ClassName(packageName, classNames + name)

    public fun member(name: String): MemberName.Member = MemberName.Member(this, name)

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

    val shortName: String get() = classNames.last()
}


public sealed class MemberName : QualifiedName() {
    public abstract val name: String

    public data class TopLevel(val packageName: PackageName, override val name: String) : MemberName() {
        override fun segments(): List<String> = packageName.segments() + name
        override fun asString(): String = buildString {
            packageName.packageNames.joinTo(this, ".")
            if (!packageName.isRoot) {
                append(".")
            }
            append(name)
        }
    }

    public data class Member(val className: ClassName, override val name: String) : MemberName() {
        val isConstructor: Boolean get() = name == SpecialNames.constructor

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
    }

    public fun sibling(name: String): MemberName = when (this) {
        is TopLevel -> TopLevel(packageName, name)
        is Member -> Member(className, name)
    }
}

public object SpecialNames {
    //TODO make sure these match what the compiler uses
    @JsName("ctor")
    public const val constructor: String = "<init>"
    public const val receiver: String = "this"
    public fun getter(property: String): String = "<get-$property>"
    public fun setter(property: String): String = "<set-$property>"
}