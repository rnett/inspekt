package dev.rnett.inspekt

import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KType
import kotlin.reflect.typeOf

/**
 * The result of inspekting a member that can be invoked like a function. Includes functions, constructors, and accessors.
 */
public sealed class FunctionLike(
    parameters: Parameters,
    isAbstract: Boolean,
    inheritedFrom: KClass<*>?,
    /**
     * The return type of the function.
     */
    public val returnType: KType,
    annotations: List<Annotation>,
    protected val invoker: ((ArgumentList) -> Any?)?,
    protected val suspendInvoker: (suspend (ArgumentList) -> Any?)? = null,
    /**
     * Whether the referenced function is suspending.
     * If true, calling [invoke] will error - use [invokeSuspend] instead.
     */
    public val isSuspend: Boolean = suspendInvoker != null,
) : Callable(parameters, isAbstract, inheritedFrom, annotations) {
    public abstract override val kotlin: KFunction<*>
    public abstract override val name: CallableName

    /**
     * Invoke the referenced function with the given parameters.
     * Will error if the underlying function is `suspend`.
     *
     * @throws FunctionInvocationException if invocation fails
     */
    public inline operator fun invoke(arguments: ArgumentsBuilder): Any? {
        contract { callsInPlace(arguments, InvocationKind.EXACTLY_ONCE) }
        return invoke(wrapInvocation { parameters.buildArguments(arguments) })
    }

    /**
     * Invoke the referenced function with the given parameters.
     * Will error if the underlying function is `suspend`.
     *
     * @throws FunctionInvocationException if invocation fails
     */
    public operator fun invoke(arguments: ArgumentList): Any? = wrapInvocation {
        if (isSuspend) throw IllegalArgumentException("Can only invoke suspend function via invokeSuspend")
        assertInvokable()
        return invoker!!.invoke(arguments)
    }

    /**
     * Invoke the referenced `suspend` function with the given parameters.
     * If the function is not suspend, this will still invoke it without an error.
     *
     * @throws FunctionInvocationException if invocation fails
     */
    public suspend inline fun invokeSuspend(arguments: ArgumentsBuilder): Any? {
        contract { callsInPlace(arguments, InvocationKind.EXACTLY_ONCE) }
        return invokeSuspend(wrapInvocation { parameters.buildArguments(arguments) })
    }

    /**
     * Invoke the referenced `suspend` function with the given parameters.
     * If the function is not suspend, this will still invoke it without an error.
     *
     * @throws FunctionInvocationException if invocation fails
     */
    public suspend fun invokeSuspend(arguments: ArgumentList): Any? = wrapInvocation {
        assertInvokable()
        return if (isSuspend)
            suspendInvoker!!.invoke(arguments)
        else
            invoker!!.invoke(arguments)
    }


    init {
        if (isCallable) {
            if (isSuspend) {
                require(invoker == null) { "Cannot specify a non-suspend invoker for a suspend function" }
                require(suspendInvoker != null) { "Must specify a suspend invoker for a suspend function" }
            } else {
                require(invoker != null) { "Must specify a non-suspend invoker for a non-suspend function" }
                require(suspendInvoker == null) { "Cannot specify a suspend invoker for a non-suspend function" }
            }
        }
    }

    protected open fun assertInvokable() {
        if (invoker == null && suspendInvoker == null) error("Method $this is abstract and cannot be invoked")
    }

    /**
     * Whether the method can be invoked. **Almost** always equal to `!isAbstract`,
     * but may be false if conditions (such as `reified` type parameters) prevent generating an invoker lambda.
     */
    public open val isCallable: Boolean get() = invoker != null || suspendInvoker != null

    protected fun StringBuilder.appendMethodSignature(includeFullName: Boolean) {
        appendContext()
        if (isSuspend) append("suspend ")
        append("fun ")
        appendExtension()

        if (includeFullName) {
            append(name)
        } else {
            append(shortName)
        }
        append("(")
        parameters.value.joinTo(this, ", ")
        append(")")
        append(": ")
        append(returnType.friendlyName)
        if (inheritedFrom != null)
            append(" @ ${inheritedFrom.friendlyName}")
    }
}

public class Constructor internal constructor(
    override val name: CallableName.Member,
    override val kotlin: KFunction<*>,
    annotations: List<Annotation>,
    isAbstract: Boolean,
    parameters: Parameters,
    returnType: KType,
    forClassRef: Lazy<Inspektion<*>>,
    /**
     * Whether this is the primary constructor.
     */
    public val isPrimary: Boolean,
    invoker: ((ArgumentList) -> Any?)?
) : FunctionLike(parameters, isAbstract, null, returnType, annotations, invoker) {

    /**
     * The class constructed by this constructor.
     */
    public val forClass: Inspektion<*> by forClassRef

    public val classTypeParameters: List<TypeParameter> get() = forClass.typeParameters

    public override fun toString(includeFullName: Boolean): String = buildString {
        append(if (includeFullName) name.toString() else "<init>")
        append("(")
        parameters.value.joinTo(this, ", ")
        append(")")
    }
}

/**
 * The result of inspekting a function-like declaration that is a normal function, not a constructor.
 */
public sealed class Function(
    parameters: Parameters,
    isAbstract: Boolean,
    inheritedFrom: KClass<*>?,
    returnType: KType,
    annotations: List<Annotation>,
    invoker: ((ArgumentList) -> Any?)?,
    suspendInvoker: (suspend (ArgumentList) -> Any?)? = null,
    isSuspend: Boolean = suspendInvoker != null,
) : FunctionLike(parameters, isAbstract, inheritedFrom, returnType, annotations, invoker, suspendInvoker, isSuspend)

/**
 * The result of inspekting simple function declaration, i.e. one declared with `fun`.
 */
public class SimpleFunction internal constructor(
    override val name: CallableName,
    override val kotlin: KFunction<*>,
    annotations: List<Annotation>,
    isAbstract: Boolean,
    parameters: Parameters,
    public val typeParameters: List<TypeParameter>,
    returnType: KType,
    inheritedFrom: KClass<*>?,
    /**
     * Whether the referenced function is suspending.
     * If true, calling [invoke] will error - use [invokeSuspend] instead.
     */
    isSuspend: Boolean,
    invoker: ((ArgumentList) -> Any?)?,
    suspendInvoker: (suspend (ArgumentList) -> Any?)?,
) : Function(parameters, isAbstract, inheritedFrom, returnType, annotations, invoker, suspendInvoker, isSuspend) {

    internal fun asSetter(property: Property): PropertySetter {
        return PropertySetter(
            lazyOf(property),
            kotlin,
            parameters,
            isAbstract,
            annotations,
            invoker!!,
            inheritedFrom
        )
    }

    internal fun asGetter(property: Property): PropertyGetter {
        return PropertyGetter(
            lazyOf(property),
            kotlin,
            parameters,
            isAbstract,
            annotations,
            invoker!!,
            inheritedFrom,
            returnType
        )
    }

    public override fun toString(includeFullName: Boolean): String = buildString {
        appendMethodSignature(includeFullName)
    }
}

/**
 * The result of inspekting a property accessor.
 */
public sealed class PropertyAccessor(
    parameters: Parameters,
    isAbstract: Boolean,
    inheritedFrom: KClass<*>?,
    returnType: KType,
    annotations: List<Annotation>,
    invoker: ((ArgumentList) -> Any?)?
) : Function(parameters, isAbstract, inheritedFrom, returnType, annotations, invoker) {
    /**
     * The property this accessor belongs to.
     */
    public abstract val property: Property
}

/**
 * The result of inspekting property getter.
 */
public class PropertyGetter internal constructor(
    propertyRef: Lazy<Property>,
    override val kotlin: KFunction<*>,
    parameters: Parameters,
    isAbstract: Boolean,
    annotations: List<Annotation>,
    invoker: ((ArgumentList) -> Any?)?,
    inheritedFrom: KClass<*>?,
    returnType: KType
) : PropertyAccessor(parameters, isAbstract, inheritedFrom, returnType, annotations, invoker) {
    public override val property: Property by propertyRef

    override val name: CallableName
        get() = property.name.sibling(SpecialNames.getter(property.name.name))

    override fun toString(includeFullName: Boolean): String = buildString {
        appendMethodSignature(includeFullName)
    }
}

/**
 * The result of inspekting property setter.
 */
public class PropertySetter internal constructor(
    propertyRef: Lazy<Property>,
    override val kotlin: KFunction<*>,
    parameters: Parameters,
    isAbstract: Boolean,
    annotations: List<Annotation>,
    invoker: ((ArgumentList) -> Any?)?,
    inheritedFrom: KClass<*>?,
) : PropertyAccessor(parameters, isAbstract, inheritedFrom, typeOf<Unit>(), annotations, invoker) {
    public override val property: Property by propertyRef

    override val name: CallableName
        get() = property.name.sibling(SpecialNames.setter(property.name.name))

    override fun toString(includeFullName: Boolean): String = buildString {
        appendMethodSignature(includeFullName)
    }
}