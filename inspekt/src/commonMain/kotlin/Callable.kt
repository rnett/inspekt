package dev.rnett.inspekt

import kotlin.reflect.KCallable
import kotlin.reflect.KClass


public sealed class Callable : AnnotatedElement {
    public abstract val kotlin: KCallable<*>
    public abstract val name: MemberName
    public val shortName: String get() = name.name
    public abstract val parameters: Parameters
    public abstract val isAbstract: Boolean
    public abstract val inheritedFrom: KClass<*>?

    public val isDeclared: Boolean get() = inheritedFrom == null
    public val isTopLevel: Boolean get() = name is MemberName.TopLevel

    public abstract fun toString(includeFullName: Boolean): String
    final override fun toString(): String {
        return toString(true)
    }

    protected fun StringBuilder.appendContext() {
        if (parameters.context.isNotEmpty()) {
            append("context(")
            parameters.context.joinTo(this, ", ")
            append(") ")
        }
    }

    protected fun StringBuilder.appendExtension() {
        val extension = parameters.extension
        if (extension != null) {
            append(extension.type)
            append(".")
        }
    }
}