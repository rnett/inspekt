package dev.rnett.spekt

import dev.rnett.symbolexport.ExportSymbol
import kotlin.reflect.KClass
import kotlin.reflect.KType

//TODO type parameters, for everything

@ExportSymbol
public data class Spekt<T : Any> internal constructor(
    public val kotlin: KClass<T>,
    public val name: ClassName,
    public val supertypes: Set<KType>,
    override val annotations: List<Annotation>,
    public val objectInstance: T?,
    public val functions: List<Function>,
    public val properties: List<Property>,
    public val constructors: List<Constructor>,
    /**
     * True if the class is an abstract class or interface.
     * Attempting to call constructors will fail.
     */
    public val isAbstract: Boolean,
    private val caster: Caster<T>,
    public val sealedSubclasses: List<Spekt<out T>>
) : AnnotatedElement {

    public val superclasses: Set<KClass<*>> = buildSet { supertypes.mapNotNullTo(this) { it.classifier as? KClass<*> } }
    public fun isSubtypeOf(other: KClass<*>): Boolean = other == Any::class || other in superclasses

    public fun cast(value: Any): T = caster.cast(value)
    public fun isInstance(value: Any): Boolean = caster.isInstance(value)
    public fun safeCast(value: Any): T? = caster.safeCast(value)

    public val shortName: String get() = name.shortName

    public val primaryConstructor: Constructor? get() = constructors.find { it.isPrimary }

    override fun toString(): String = toString(false)

    public fun toString(includeSubclasses: Boolean, includeDescendents: Boolean = includeSubclasses): String = buildString {
        append(if (isAbstract) "abstract class " else if (objectInstance != null) "object " else "class ")
        append(name.asString())
        append(" : ")
        supertypes.joinTo(this, ", ") { it.friendlyName }
        appendLine(" {")
        constructors.forEach { append("    ").appendLine(it) }
        functions.forEach { append("    ").appendLine(it) }
        properties.forEach { append("    ").appendLine(it) }
        append("}")

        if (includeSubclasses && sealedSubclasses.isNotEmpty()) {
            appendLine()
            appendLine()
            sealedSubclasses.forEach {
                appendLine(it.toString(includeDescendents))
                appendLine()
            }
        }
    }.trim()

}

@ExportSymbol
@PublishedApi
internal interface Caster<T : Any> {
    @ExportSymbol
    fun cast(@ExportSymbol value: Any): T

    @ExportSymbol
    fun isInstance(@ExportSymbol value: Any): Boolean

    @ExportSymbol
    fun safeCast(@ExportSymbol value: Any): T?
}