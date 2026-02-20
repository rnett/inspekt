package dev.rnett.inspekt.proxy

import dev.rnett.inspekt.ArgumentList
import dev.rnett.inspekt.ArgumentsBuilder
import dev.rnett.inspekt.Function
import dev.rnett.inspekt.Method
import dev.rnett.inspekt.Parameter
import dev.rnett.inspekt.Parameters
import dev.rnett.inspekt.Property
import dev.rnett.inspekt.PropertyAccessor
import dev.rnett.inspekt.PropertyGetter
import dev.rnett.inspekt.PropertySetter
import dev.rnett.inspekt.Spekt
import dev.rnett.inspekt.SpektCompilerPluginIntrinsic
import dev.rnett.inspekt.throwIntrinsicException
import dev.rnett.symbolexport.ExportSymbol
import kotlin.reflect.KClass

public sealed class SuperCall {
    public abstract val superMethod: Method
    public val parameters: Parameters get() = superMethod.parameters
    public abstract val args: List<Any?>

    public val isSuperCallable: Boolean get() = !superMethod.isAbstract
    public val isSuperAbstract: Boolean get() = superMethod.isAbstract

    public fun callSuper(): Any? {
        return superMethod.invoke(parameters.arguments(args))
    }

    public fun ArgumentList.Builder.copyArgs() {
        setAll(args)
    }

    public inline fun callSuper(builder: ArgumentsBuilder): Any? = superMethod.invoke(builder)

    public sealed class PropertyAccess : SuperCall() {
        public abstract val superProperty: Property
        public abstract override val superMethod: PropertyAccessor
    }

    public data class FunctionCall(val superFunction: Function, override val args: List<Any?>) : SuperCall() {
        override val superMethod: Function get() = superFunction
    }

    /**
     * Note that [superGetter] and `property.getter` may not be the same, if the original getter was overridden by an intermediate superclass.
     * [superGetter] is the method that the getter is overridden, while [superProperty] is the original property that is being overridden.
     */
    public data class PropertyGet(override val superProperty: Property, override val superMethod: PropertyGetter, override val args: List<Any?>) : PropertyAccess() {
        val superGetter: PropertyGetter get() = superMethod
    }

    /**
     * Note that [superSetter] and `property.setter` may not be the same, if the original setter was overridden (or added!) by an intermediate superclass.
     * [superSetter] is the method that the setter is overridden, while [superProperty] is the original property that is being overridden (which may even be a `val`).
     */
    public data class PropertySet(override val superProperty: Property, override val superMethod: PropertySetter, override val args: List<Any?>) : PropertyAccess() {
        val superSetter: PropertySetter get() = superMethod
    }
}

@ExportSymbol
public fun interface ProxyHandler {
    /**
     * The [Method.invoke] method on `this.method`, `this.getter`, etc. will throw an error if the method is abstract (i.e. doesn't have a default), and will call the default otherwise.
     */
    public fun SuperCall.handle(): Any?

    /**
     * The [Method.invoke] method on `this.method`, `this.getter`, etc. will throw an error if the method is abstract (i.e. doesn't have a default), and will call the default otherwise.
     */
    public suspend fun SuperCall.handleSuspend(): Any? = handle()

    context(superCall: SuperCall)
    public operator fun Array<Any?>.get(param: Parameter): Any? = this[param.globalIndex]
}

@Suppress("unused")
@ExportSymbol
@PublishedApi
internal fun v1ProxyHelper(
    @ExportSymbol handler: ProxyHandler,
    @ExportSymbol originalMethod: Function,
    @ExportSymbol originalProperty: Property?,
    @ExportSymbol isSetter: Boolean,
    @ExportSymbol args: Array<Any?>
): Any? {
    val argsList = args.asList()
    val original = when {
        originalProperty == null -> SuperCall.FunctionCall(originalMethod, argsList)
        isSetter -> SuperCall.PropertySet(originalProperty, originalMethod.asSetter(originalProperty), argsList)
        else -> SuperCall.PropertyGet(originalProperty, originalMethod.asGetter(originalProperty), argsList)
    }
    return handler.run {
        original.handle()
    }
}

// params must match v1ProxyHelper
@Suppress("unused")
@ExportSymbol
@PublishedApi
internal suspend fun v1SuspendProxyHelper(
    handler: ProxyHandler,
    originalMethod: Function,
    originalProperty: Property?,
    isSetter: Boolean,
    args: Array<Any?>
): Any? {
    val argsList = args.asList()
    val original = when {
        originalProperty == null -> SuperCall.FunctionCall(originalMethod, argsList)
        isSetter -> SuperCall.PropertySet(originalProperty, originalMethod.asSetter(originalProperty), argsList)
        else -> SuperCall.PropertyGet(originalProperty, originalMethod.asGetter(originalProperty), argsList)
    }
    return handler.run {
        original.handleSuspend()
    }
}

/**
 * [toImplement] and each [additionalInterfaces] must be an interface.
 */
@ExportSymbol
@SpektCompilerPluginIntrinsic
public fun <T : Any> proxy(@ExportSymbol toImplement: KClass<T>, @ExportSymbol vararg additionalInterfaces: KClass<*>, @ExportSymbol handler: ProxyHandler): T = throwIntrinsicException()

/**
 * [toImplement] and each [additionalInterfaces] must be an interface.
 */
@ExportSymbol
@SpektCompilerPluginIntrinsic
public fun <T : Any> proxyFactory(@ExportSymbol toImplement: KClass<T>, @ExportSymbol vararg additionalInterfaces: KClass<*>): (ProxyHandler) -> T = throwIntrinsicException()

public class ProxyableSpekt<T : Any>(public val spekt: Spekt<T>, private val factory: (ProxyHandler) -> T) {
    public fun proxy(handler: ProxyHandler): T = factory(handler)
}

/**
 * [toImplement] must be an interface.
 */
@ExportSymbol
@SpektCompilerPluginIntrinsic
public fun <T : Any> proxyableSpekt(@ExportSymbol toImplement: KClass<T>): ProxyableSpekt<T> = throwIntrinsicException()


@Suppress("UNCHECKED_CAST", "unused")
@ExportSymbol
@PublishedApi
internal fun <T : Any> v1ProxyableSpektHelper(
    spekt: Spekt<*>,
    proxyFactory: (ProxyHandler) -> Any?
): ProxyableSpekt<T> = ProxyableSpekt(spekt as Spekt<T>, proxyFactory as (ProxyHandler) -> T)