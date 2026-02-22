package dev.rnett.inspekt

import dev.rnett.symbolexport.ExportSymbol
import kotlin.reflect.KClass
import kotlin.reflect.KType

/**
 * The result of inspekting a class.
 */
@ExportSymbol
public class Inspektion<T : Any> internal constructor(
    public val kotlin: KClass<T>,
    public val name: ClassName,
    public val supertypes: Set<KType>,
    override val annotations: List<Annotation>,
    public val typeParameters: List<TypeParameter>,
    public val objectInstance: T?,
    public val functions: List<SimpleFunction>,
    public val properties: List<Property>,
    public val constructors: List<Constructor>,
    /**
     * True if the class is an abstract class or interface.
     * Attempting to call constructors will fail.
     */
    public val isAbstract: Boolean,
    public val sealedSubclasses: List<Inspektion<out T>>,
    private val cast: (Any) -> T,
    private val isInstance: (Any) -> Boolean,
    private val safeCast: (Any) -> T?,
    public val companionObject: Inspektion<Any>?,
) : AnnotatedElement {

    public val superclasses: Set<KClass<*>> = buildSet { supertypes.mapNotNullTo(this) { it.classifier as? KClass<*> } }
    public fun isSubtypeOf(other: KClass<*>): Boolean = other == Any::class || other in superclasses

    public fun cast(value: Any): T = cast.invoke(value)
    public fun isInstance(value: Any): Boolean = isInstance.invoke(value)
    public fun safeCast(value: Any): T? = safeCast.invoke(value)

    public val shortName: String get() = name.simpleName

    public val primaryConstructor: Constructor? get() = constructors.find { it.isPrimary }

    public val companionObjectInstance: Any? get() = companionObject?.objectInstance

    public val declaredFunctions: List<SimpleFunction> get() = functions.filter { it.isDeclared }
    public val declaredProperties: List<Property> get() = properties.filter { it.isDeclared }

    public fun property(name: String): Property = properties.single { it.shortName == name }
    public fun function(name: String): SimpleFunction = functions.single { it.shortName == name }

    override fun toString(): String = toString(false)

    public fun toString(includeSubclasses: Boolean, includeDescendents: Boolean = includeSubclasses): String = buildString {
        append(if (isAbstract) "abstract class " else if (objectInstance != null) "object " else "class ")
        append(name.asString())
        append(" : ")
        supertypes.joinTo(this, ", ") { it.friendlyName }
        appendLine(" {")
        constructors.forEach { append("    ").appendLine(it.toString(false)) }
        functions.forEach { append("    ").appendLine(it.toString(false)) }
        properties.forEach { append("    ").appendLine(it.toString(false)) }
        if (companionObject != null) {
            appendLine()
            appendLine("    companion object")
        }
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

    override fun equals(other: Any?): Boolean {
        if (other !is Inspektion<*>) return false
        if (this.name != other.name) return false
        return this.kotlin == other.kotlin
    }

    override fun hashCode(): Int = kotlin.hashCode()
}