package dev.rnett.inspekt

import kotlin.reflect.KCallable
import kotlin.reflect.KClass

/**
 * The result of inspekting a function-like or property declaration.
 */
public sealed class Callable : AnnotatedElement {
    /**
     * A reference to the Kotlin declaration this represents.
     */
    public abstract val kotlin: KCallable<*>

    /**
     * The fully qualified name of the declaration.
     */
    public abstract val name: CallableName

    /**
     * The short name of the declaration.
     */
    public val shortName: String get() = name.name

    /**
     * The parameters of the declaration.
     */
    public abstract val parameters: Parameters

    /**
     * Whether the declaration is abstract.
     */
    public abstract val isAbstract: Boolean

    /**
     * If the declaration is inherited from a superclass (**not** overridden), the superclass it is inherited from.
     */
    public abstract val inheritedFrom: KClass<*>?

    /**
     * Whether the declaration is declared in its owning class. False if it is inherited.
     */
    public val isDeclared: Boolean get() = inheritedFrom == null

    /**
     * Whether the declaration is top-level (not a member of a class).
     */
    public val isTopLevel: Boolean get() = name is CallableName.TopLevel

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