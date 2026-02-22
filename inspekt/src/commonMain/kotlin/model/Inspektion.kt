package dev.rnett.inspekt.model

import dev.rnett.inspekt.FunctionInvocationException
import dev.rnett.inspekt.friendlyName
import dev.rnett.symbolexport.ExportSymbol
import kotlin.reflect.KClass
import kotlin.reflect.KType

/**
 * The result of inspekting a class.
 */
@ExportSymbol
public class Inspektion<T : Any> internal constructor(
    /**
     * The class that was inspekted.
     */
    public val kotlin: KClass<T>,
    /**
     * The qualified name of the class.
     */
    public val name: ClassName,
    /**
     * The class's supertypes.
     */
    public val supertypes: Set<KType>,
    /**
     * The annotations on the class.
     */
    override val annotations: List<Annotation>,
    /**
     * The class's type parameters.
     */
    public val typeParameters: List<TypeParameter>,
    /**
     * The object instance, if the class is an object.
     */
    public val objectInstance: T?,
    /**
     * The class's functions.
     * Includes functions inherited from superclasses.
     * Does not include property accessors.
     *
     * @see SimpleFunction.isDeclared
     */
    public val functions: List<SimpleFunction>,
    /**
     * The class's properties.
     * Includes properties inherited from superclasses.
     *
     * @see Property.isDeclared
     */
    public val properties: List<Property>,
    /**
     * The class's constructors.
     */
    public val constructors: List<Constructor>,
    /**
     * True if the class is an abstract class or interface.
     * If true, attempting to call constructors will fail.
     * Constructors may still be present for abstract classes.
     */
    public val isAbstract: Boolean,
    /**
     * Any sealed (direct) subclasses of this class.
     */
    public val sealedSubclasses: List<Inspektion<out T>>,
    private val cast: (Any) -> T,
    private val isInstance: (Any) -> Boolean,
    private val safeCast: (Any) -> T?,
    /**
     * The companion object instance, if this class has one.
     *
     * @see companionObjectInstance
     */
    public val companionObject: Inspektion<Any>?,
) : AnnotatedElement {

    /**
     * The class's superclasses, as [KClass]s.
     */
    public val superclasses: Set<KClass<*>> = buildSet { supertypes.mapNotNullTo(this) { it.classifier as? KClass<*> } }

    /**
     * Returns true if this is a subtype of [other].
     */
    public fun isSubtypeOf(other: KClass<*>): Boolean = other == Any::class || other in superclasses

    /**
     * Casts [value] to this class.
     * This is the same as using the `as` operator.
     * Can be checked ahead of time with [isInstance] (aka `is`), and done safely with [safeCast] (aka `as?`).
     *
     * @throw ClassCastException if [value] is not an instance of this class.
     * @see isInstance
     * @see safeCast
     */
    public fun cast(value: Any): T = cast.invoke(value)

    /**
     * Returns true if [value] is an instance of this class.
     * This is the same as using the `is` operator.
     *
     * @see cast
     * @see safeCast
     */
    public fun isInstance(value: Any): Boolean = isInstance.invoke(value)

    /**
     * Attempts to cast [value] to this class, and returns `null` if it is not possible.
     * This is the same as using the `as?` operator.
     *
     * @see isInstance
     * @see cast
     */
    public fun safeCast(value: Any): T? = safeCast.invoke(value)

    /**
     * The short name of this class, i.e. its own declaration name.
     */
    public val shortName: String get() = name.simpleName

    /**
     * The primary constructor of this class, if it has one.
     */
    public val primaryConstructor: Constructor? get() = constructors.find { it.isPrimary }

    /**
     * The companion object instance, if this class has one.
     *
     * @see companionObject
     */
    public val companionObjectInstance: Any? get() = companionObject?.objectInstance

    /**
     * The functions that were declared in this class.
     * Includes overrides.
     * Does not include functions that were just inherited from supertypes.
     *
     * @see functions
     * @see SimpleFunction.isDeclared
     */
    public val declaredFunctions: List<SimpleFunction> get() = functions.filter { it.isDeclared }

    /**
     * The properties that were declared in this class.
     * Includes overrides.
     * Does not include properties that were just inherited from supertypes.
     *
     * @see properties
     * @see Property.isDeclared
     */
    public val declaredProperties: List<Property> get() = properties.filter { it.isDeclared }

    /**
     * Gets the single property named [name].
     *
     * @throws NoSuchElementException if no property with that name exists.
     * @throws IllegalArgumentException if more than one property with that name exists.
     */
    public fun property(name: String): Property = properties.single { it.shortName == name }

    /**
     * Gets the single function named [name].
     *
     * @throws NoSuchElementException if no function with that name exists.
     * @throws IllegalArgumentException if more than one function with that name exists.
     */
    public fun function(name: String): SimpleFunction = functions.single { it.shortName == name }

    /**
     * Call the single function named [name] with [receiver] and [argumentsBuilder].
     *
     * If the function is static, [receiver] is ignored.
     * [receiver] must be non-null for non-static functions.
     *
     * @throws dev.rnett.inspekt.FunctionInvocationException if the function cannot be invoked or the function is suspending.
     * @throws NoSuchElementException if no function with that name exists.
     * @throws IllegalArgumentException if more than one function with that name exist.
     * @see function
     * @see Function.invoke
     */
    public inline fun callFunction(receiver: T?, name: String, argumentsBuilder: ArgumentsBuilder = {}): Any? = function(name).invoke {
        if (it.hasDispatch && receiver != null)
            dispatchReceiver = receiver
        argumentsBuilder(it)
    }

    /**
     * Call the single function named [name] with [receiver] and [argumentsBuilder].
     *
     * If the function is static, [receiver] is ignored.
     * [receiver] must be non-null for non-static functions.
     *
     * @throws dev.rnett.inspekt.FunctionInvocationException if the function cannot be invoked.
     * @throws NoSuchElementException if no function with that name exists.
     * @throws IllegalArgumentException if more than one function with that name exist.
     * @see function
     * @see Function.invokeSuspend
     */
    public suspend inline fun callSuspendFunction(receiver: T?, name: String, argumentsBuilder: ArgumentsBuilder = {}): Any? = function(name).invokeSuspend {
        if (it.hasDispatch && receiver != null)
            dispatchReceiver = receiver
        argumentsBuilder(it)
    }

    /**
     * Call the single function named [name] with [receiver] and [argumentsBuilder].
     *
     * If the function is static, [receiver] is ignored.
     * [receiver] must be non-null for non-static functions, or be set in [argumentsBuilder].
     *
     * @throws dev.rnett.inspekt.FunctionInvocationException if the function cannot be invoked or is suspending.
     * @throws NoSuchElementException if no function with that name exists.
     * @throws IllegalArgumentException if more than one function with that name exist.
     * @see function
     * @see Function.invoke
     */
    public inline operator fun invoke(receiver: T?, name: String, argumentsBuilder: ArgumentsBuilder = {}): Any? = callFunction(receiver, name, argumentsBuilder)

    /**
     * Calls the primary constructor of this class with [argumentsBuilder].
     *
     * @throws dev.rnett.inspekt.FunctionInvocationException if the class has no primary constructor or it cannot be invoked.
     * @see Function.invoke
     * @see primaryConstructor
     */
    @Suppress("UNCHECKED_CAST")
    public inline fun callPrimaryConstructor(argumentsBuilder: ArgumentsBuilder = {}): T =
        (primaryConstructor ?: throw FunctionInvocationException(name, IllegalStateException("Class $name has no primary constructor"))).invoke(argumentsBuilder) as T

    /**
     * Calls the primary constructor of this class with [argumentsBuilder].
     *
     * @throws FunctionInvocationException if the class has no primary constructor or it cannot be invoked.
     * @see Function.invoke
     * @see primaryConstructor
     */
    @Suppress("UNCHECKED_CAST")
    public inline operator fun invoke(argumentsBuilder: ArgumentsBuilder = {}): T = callPrimaryConstructor(argumentsBuilder)

    /**
     * Gets the value of the single property named [name] with [receiver] and [argumentsBuilder].
     *
     * If the property is static, [receiver] is ignored.
     * [receiver] must be non-null for non-static properties, or be set in [argumentsBuilder].
     *
     * @throws FunctionInvocationException if the property getter cannot be invoked.
     * @throws NoSuchElementException if no property with that name exists.
     * @throws IllegalArgumentException if more than one property with that name exist.
     * @see property
     * @see Property.get
     * @see Function.invoke
     */
    public inline fun getProperty(receiver: T?, name: String, crossinline argumentsBuilder: ArgumentsBuilder = {}): Any? = property(name).get {
        if (it.hasDispatch && receiver != null)
            dispatchReceiver = receiver
        argumentsBuilder(it)
    }

    /**
     * Gets the value of the single property named [name] with [receiver] and [argumentsBuilder].
     *
     * If the property is static, [receiver] is ignored.
     * [receiver] must be non-null for non-static properties, or be set in [argumentsBuilder].
     *
     * @throws FunctionInvocationException if the property getter cannot be invoked.
     * @throws NoSuchElementException if no property with that name exists.
     * @throws IllegalArgumentException if more than one property with that name exist.
     * @see property
     * @see Property.get
     * @see Function.invoke
     */
    public inline operator fun get(receiver: T?, name: String, crossinline argumentsBuilder: ArgumentsBuilder = {}): Any? = getProperty(receiver, name, argumentsBuilder)

    /**
     * Sets the value of the single property named [name] with [receiver] and [argumentsBuilder].
     *
     * If the property is static, [receiver] is ignored.
     * [receiver] must be non-null for non-static properties, or be set in [argumentsBuilder].
     *
     * @throws FunctionInvocationException if the property setter cannot be invoked or the property is `val`.
     * @throws NoSuchElementException if no property with that name exists.
     * @throws IllegalArgumentException if more than one property with that name exist.
     * @see property
     * @see MutableProperty.set
     * @see Function.invoke
     */
    public inline fun setProperty(receiver: T?, name: String, crossinline argumentsBuilder: ArgumentsBuilder = {}) {
        val prop = property(name)
        prop as? MutableProperty ?: throw FunctionInvocationException(prop.name, IllegalStateException("Property $name is not mutable"))
        prop.set {
            if (it.hasDispatch && receiver != null)
                dispatchReceiver = receiver
            argumentsBuilder(it)
        }
    }

    /**
     * Sets the value of the single property named [name] to [value] with [receiver] and [argumentsBuilder] additional arguments.
     *
     * If the property is static, [receiver] is ignored.
     * [receiver] must be non-null for non-static properties, or be set in [argumentsBuilder].
     *
     * @throws FunctionInvocationException if the property setter cannot be invoked or the property is `val`.
     * @throws NoSuchElementException if no property with that name exists.
     * @throws IllegalArgumentException if more than one property with that name exist.
     * @see property
     * @see MutableProperty.set
     * @see Function.invoke
     */
    public inline fun setProperty(receiver: T?, name: String, value: Any?, crossinline argumentsBuilder: ArgumentsBuilder = {}): Unit = setProperty(receiver, name) {
        value(value)
        argumentsBuilder(it)
    }


    /**
     * Sets the value of the single property named [name] to [value] with [receiver].
     *
     * If the property is static, [receiver] is ignored.
     * [receiver] must be non-null for non-static properties.
     *
     * @throws FunctionInvocationException if the property setter cannot be invoked or the property is `val`.
     * @throws NoSuchElementException if no property with that name exists.
     * @throws IllegalArgumentException if more than one property with that name exist.
     * @see property
     * @see MutableProperty.set
     * @see Function.invoke
     */
    public operator fun set(receiver: T?, name: String, value: Any?): Unit = setProperty(receiver, name, value)

    /**
     * Sets the value of the single property named [name] with [receiver] and arguments [argumentsBuilder].
     *
     * If the property is static, [receiver] is ignored.
     * [receiver] must be non-null for non-static properties, or be set in [argumentsBuilder].
     *
     * @throws FunctionInvocationException if the property setter cannot be invoked or the property is `val`.
     * @throws NoSuchElementException if no property with that name exists.
     * @throws IllegalArgumentException if more than one property with that name exist.
     * @see property
     * @see MutableProperty.set
     * @see Function.invoke
     */
    public operator fun set(receiver: T?, name: String, argumentsBuilder: ArgumentsBuilder): Unit = setProperty(receiver, name, argumentsBuilder)

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