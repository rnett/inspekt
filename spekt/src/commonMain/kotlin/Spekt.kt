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
    public val sealedSubclasses: List<Spekt<out T>>,
    private val cast: (Any) -> T,
    private val isInstance: (Any) -> Boolean,
    private val safeCast: (Any) -> T?,
    val companionObject: Spekt<Any>?,
) : AnnotatedElement {

    public val superclasses: Set<KClass<*>> = buildSet { supertypes.mapNotNullTo(this) { it.classifier as? KClass<*> } }
    public fun isSubtypeOf(other: KClass<*>): Boolean = other == Any::class || other in superclasses

    public fun cast(value: Any): T = cast.invoke(value)
    public fun isInstance(value: Any): Boolean = isInstance.invoke(value)
    public fun safeCast(value: Any): T? = safeCast.invoke(value)

    public val shortName: String get() = name.shortName

    public val primaryConstructor: Constructor? get() = constructors.find { it.isPrimary }

    public val companionObjectInstance: Any? get() = companionObject?.objectInstance

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