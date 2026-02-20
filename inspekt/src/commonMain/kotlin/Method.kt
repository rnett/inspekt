package dev.rnett.inspekt

import dev.rnett.symbolexport.ExportSymbol
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KType
import kotlin.reflect.typeOf

public sealed class Method : Callable() {
    public abstract override val kotlin: KFunction<*>
    public abstract override val name: MemberName

    public enum class Kind {
        NORMAL,
        CONSTRUCTOR,
        GETTER,
        SETTER
    }

    public abstract val kind: Kind

    public inline operator fun invoke(arguments: ArgumentsBuilder): Any? {
        contract { callsInPlace(arguments, InvocationKind.EXACTLY_ONCE) }
        return invoke(parameters.buildArguments(arguments))
    }

    public open operator fun invoke(arguments: ArgumentList): Any? {
        assertInvokable()
        return invoker!!.invoke(arguments)
    }

    protected abstract val invoker: ((ArgumentList) -> Any?)?

    protected open fun assertInvokable() {
        if (invoker == null) error("Method $this is abstract and cannot be invoked")
    }

    /**
     * Whether the method can be invoked. Almost always equal to `!isAbstract`.
     */
    public open val isCallable: Boolean get() = invoker != null

    public abstract val returnType: KType

    protected fun StringBuilder.appendMethodSignature(includeFullName: Boolean) {
        appendContext()
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
            append(" @ ${inheritedFrom!!.friendlyName}")
    }
}

public data class Constructor internal constructor(
    override val name: MemberName.Member,
    override val kotlin: KFunction<*>,
    override val annotations: List<Annotation>,
    override val isAbstract: Boolean,
    override val parameters: Parameters,
    override val returnType: KType,
    private val forClassRef: Lazy<Inspektion<*>>,
    public val isPrimary: Boolean,
    override val invoker: ((ArgumentList) -> Any?)?
) : Method() {
    override val inheritedFrom: KClass<*>? = null
    public val forClass: Inspektion<*> by forClassRef
    override val kind: Kind get() = Kind.CONSTRUCTOR

    public override fun toString(includeFullName: Boolean): String = buildString {
        append(if (includeFullName) name.toString() else "<init>")
        append("(")
        parameters.value.joinTo(this, ", ")
        append(")")
    }
}

public sealed class SimpleFunction : Method()

@ExportSymbol
public data class Function internal constructor(
    override val name: MemberName,
    override val kotlin: KFunction<*>,
    override val annotations: List<Annotation>,
    override val isAbstract: Boolean,
    override val parameters: Parameters,
    override val returnType: KType,
    public val isSuspend: Boolean,
    override val invoker: ((ArgumentList) -> Any?)?,
    private val suspendInvoker: (suspend (ArgumentList) -> Any?)?,
    override val inheritedFrom: KClass<*>?,
) : SimpleFunction() {
    override val kind: Kind get() = Kind.NORMAL

    init {
        if (isSuspend) {
            require(invoker == null) { "Cannot specify a non-suspend invoker for a suspend function" }
            require(suspendInvoker != null) { "Must specify a suspend invoker for a suspend function" }
        } else {
            require(invoker != null) { "Must specify a non-suspend invoker for a non-suspend function" }
            require(suspendInvoker == null) { "Cannot specify a suspend invoker for a non-suspend function" }
        }
    }

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
            inheritedFrom
        )
    }

    override val isCallable: Boolean
        get() = suspendInvoker != null || invoker != null

    override fun assertInvokable() {
        // super call checks that invoker is not null
        if (suspendInvoker == null) super.assertInvokable()
    }

    override fun invoke(arguments: ArgumentList): Any? {
        assertInvokable()
        if (isSuspend) throw IllegalArgumentException("Can only invoke suspend function via invokeSuspend")
        return super.invoke(arguments)
    }

    public suspend inline fun invokeSuspend(arguments: ArgumentsBuilder): Any? {
        contract { callsInPlace(arguments, InvocationKind.EXACTLY_ONCE) }
        return invokeSuspend(parameters.buildArguments(arguments))
    }

    public suspend fun invokeSuspend(arguments: ArgumentList): Any? {
        assertInvokable()
        return if (isSuspend)
            suspendInvoker!!.invoke(arguments)
        else
            invoker!!.invoke(arguments)
    }

    public override fun toString(includeFullName: Boolean): String = buildString {
        if (isSuspend) append("suspend ")
        appendMethodSignature(includeFullName)
    }
}

public sealed class PropertyAccessor : SimpleFunction() {}

public data class PropertyGetter internal constructor(
    private val propertyRef: Lazy<Property>,
    override val kotlin: KFunction<*>,
    override val parameters: Parameters,
    override val isAbstract: Boolean,
    override val annotations: List<Annotation>,
    override val invoker: ((ArgumentList) -> Any?)?,
    override val inheritedFrom: KClass<*>?,
) : PropertyAccessor() {
    override val kind: Kind = Kind.GETTER
    public val property: Property by propertyRef

    override val returnType: KType
        get() = property.type

    override val name: MemberName
        get() = property.name.sibling(SpecialNames.getter(property.name.name))

    override fun toString(includeFullName: Boolean): String = buildString {
        appendMethodSignature(includeFullName)
    }
}

public data class PropertySetter internal constructor(
    private val propertyRef: Lazy<Property>,
    override val kotlin: KFunction<*>,
    override val parameters: Parameters,
    override val isAbstract: Boolean,
    override val annotations: List<Annotation>,
    override val invoker: ((ArgumentList) -> Any?)?,
    override val inheritedFrom: KClass<*>?,
) : PropertyAccessor() {
    override val kind: Kind = Kind.SETTER
    public val property: Property by propertyRef

    override val returnType: KType
        get() = typeOf<Unit>()

    override val name: MemberName
        get() = property.name.sibling(SpecialNames.setter(property.name.name))

    override fun toString(includeFullName: Boolean): String = buildString {
        appendMethodSignature(includeFullName)
    }
}