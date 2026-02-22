package dev.rnett.inspekt.proxy

import dev.rnett.inspekt.ArgumentList
import dev.rnett.inspekt.ArgumentsBuilder
import dev.rnett.inspekt.Function
import dev.rnett.inspekt.InspektCompilerPluginIntrinsic
import dev.rnett.inspekt.InspektNotIntrinsifiedException
import dev.rnett.inspekt.Inspektion
import dev.rnett.inspekt.InvocationFailureException
import dev.rnett.inspekt.Parameters
import dev.rnett.inspekt.Property
import dev.rnett.inspekt.PropertyAccessor
import dev.rnett.inspekt.PropertyGetter
import dev.rnett.inspekt.PropertySetter
import dev.rnett.inspekt.ReferenceLiteral
import dev.rnett.inspekt.SimpleFunction
import dev.rnett.inspekt.inspekt
import dev.rnett.symbolexport.ExportSymbol
import kotlin.reflect.KClass

public sealed class SuperCall {
    /**
     * The function originally being called.
     */
    public abstract val superFun: Function

    /**
     * The parameters of the function originally being called.
     */
    public val parameters: Parameters get() = superFun.parameters

    /**
     * The arguments the function is being called with.
     */
    public abstract val args: ArgumentList

    /**
     * Whether [superFun] can be invoked.
     */
    public val isSuperCallable: Boolean get() = superFun.isCallable

    /**
     * Whether [superFun] is abstract.
     */
    public val isSuperAbstract: Boolean get() = superFun.isAbstract

    /**
     * Call [superFun] with the call's arguments.
     */
    public fun callSuper(): Any? {
        return superFun.invoke(args)
    }

    /**
     * Call [superFun] with the call's arguments.
     */
    public suspend fun callSuperSuspend(): Any? {
        return superFun.invokeSuspend(args)
    }

    /**
     * Copy the call's arguments into this argument list.
     */
    public fun ArgumentList.Builder.copyArgs() {
        setAll(args)
    }

    /**
     * Call [superFun] with the given arguments.
     */
    public inline fun callSuper(builder: ArgumentsBuilder): Any? = superFun.invoke(builder)

    public sealed class PropertyAccess : SuperCall() {
        public abstract val superProperty: Property
        public abstract override val superFun: PropertyAccessor
    }

    /**
     * A call to a simple function.
     */
    public data class FunctionCall(val superFunction: SimpleFunction, override val args: ArgumentList) : SuperCall() {
        override val superFun: SimpleFunction get() = superFunction
    }

    /**
     * A call to a property's getter.
     *
     * Note that [superGetter] and `superProperty.getter` may not be the same, if the original getter was overridden by an intermediate superclass.
     * [superGetter] is the getter that the getter is overriding, while [superProperty] is the original property that is being overridden.
     * **Any calls should go to [superGetter]**, which is set as [superFun].
     */
    public data class PropertyGet(override val superProperty: Property, override val superFun: PropertyGetter, override val args: ArgumentList) : PropertyAccess() {
        val superGetter: PropertyGetter get() = superFun
    }

    /**
     * A call to a property's setter.
     *
     * Note that [superSetter] and `superProperty.setter` may not be the same, if the original setter was defined or overridden by an intermediate superclass - [superProperty] may not even have a setter.
     * [superSetter] is the setter that the setter is overriding, while [superProperty] is the original property that is being overridden.
     * **Any calls should go to [superSetter]**, which is set as [superFun].
     */
    public data class PropertySet(override val superProperty: Property, override val superFun: PropertySetter, override val args: ArgumentList) : PropertyAccess() {
        val superSetter: PropertySetter get() = superFun
    }
}

/**
 * A handler for calls to Inspekt proxies. Will be called for all method or property accessors on the proxy object.
 */
@ExportSymbol
public fun interface ProxyHandler {
    /**
     * Handle a call to this proxy.
     *
     * If you call super, make sure you check [SuperCall.isSuperCallable] first.
     * Calls to abstract super methods will throw [InvocationFailureException].
     */
    public fun SuperCall.handle(): Any?

    /**
     * Handle a suspending call to this proxy. Defaults to calling [handle].
     *
     * If you call super, make sure you check [SuperCall.isSuperCallable] first.
     * Calls to abstract super methods will throw [InvocationFailureException].
     */
    public suspend fun SuperCall.handleSuspend(): Any? = handle()
}

/**
 * Create a proxy object that implements [T] and responds to **all** method or accessor calls using [handler].
 * [toImplement] and each [additionalInterfaces] **must be a class reference literal of an interface**.
 *
 * The resulting object will implement [toImplement] and each [additionalInterfaces], and can be safely cast to types from [additionalInterfaces].
 *
 * A call to this method is transformed into an anonymous object instance by the compiler plugin.
 * All the arguments are calculated at compilation time.
 *
 * Because of this, calls of this function can potentially add a significant amount of binary size.
 * This is based on the number of appearances of `proxy()` in your code, not how many times it is invoked.
 * If you find yourself repeatedly creating proxies for the same class, consider using [proxyFactory], which has a constant binary overhead per factory invocation.
 *
 * @throws InspektNotIntrinsifiedException if it was not intrinsified by the Inspekt compiler plugin.
 */
@ExportSymbol
@InspektCompilerPluginIntrinsic
public fun <T : Any> proxy(
    @ReferenceLiteral(mustBeInterface = true) toImplement: KClass<T>,
    @ReferenceLiteral(mustBeInterface = true) vararg additionalInterfaces: KClass<*>,
    handler: ProxyHandler
): T = throw InspektNotIntrinsifiedException()


/**
 * Create a proxy object factory, for proxies that implements [T] and responds to **all** method or accessor calls using the handler passed to the factory.
 * [toImplement] and each [additionalInterfaces] **must be a class reference literal of an interface**.
 *
 * The resulting object will implement [toImplement] and each [additionalInterfaces], and can be safely cast to types from [additionalInterfaces].
 *
 * A call to this method is transformed into an anonymous object instance by the compiler plugin.
 * All the arguments are calculated at compilation time.
 *
 * Because of this, calls of this function can potentially add a significant amount of binary size.
 * This is based on the number of appearances of `proxyFactory()` in your code, not how many times it is invoked.
 * **Calling the factory does not add overhead**, only the `proxyFactory` call.
 *
 * @throws InspektNotIntrinsifiedException if it was not intrinsified by the Inspekt compiler plugin.
 * @see proxy
 */
@ExportSymbol
@InspektCompilerPluginIntrinsic
public fun <T : Any> proxyFactory(
    @ReferenceLiteral(mustBeInterface = true) toImplement: KClass<T>,
    @ReferenceLiteral(mustBeInterface = true) vararg additionalInterfaces: KClass<*>
): (ProxyHandler) -> T = throw InspektNotIntrinsifiedException()

/**
 * An [Inspektion] of a class, and a method for creating proxies of it.
 */
public class ProxyableInspektion<T : Any>(public val inspektion: Inspektion<T>, private val factory: (ProxyHandler) -> T) {
    public fun createProxy(handler: ProxyHandler): T = factory(handler)
}

/**
 * Create an [Inspektion] and a proxy object factory, for proxies that implements [T] and responds to **all** method or accessor calls using the handler passed to the factory.
 * [toImplement] **must be a class reference literal of an interface**.
 *
 * A call to this method is transformed into an anonymous object instance by the compiler plugin.
 * All the arguments are calculated at compilation time.
 *
 * Because of this, calls of this function can potentially add a significant amount of binary size.
 * This is based on the number of appearances of `inspektAndProxy()` in your code, not how many times it is invoked.
 * **Calling the factory does not add overhead**, only the `inspektAndProxy` call.
 *
 * @throws InspektNotIntrinsifiedException if it was not intrinsified by the Inspekt compiler plugin.
 * @see inspekt
 * @see proxyFactory
 */
@ExportSymbol
@InspektCompilerPluginIntrinsic
public fun <T : Any> inspektAndProxy(@ReferenceLiteral(mustBeInterface = true) toImplement: KClass<T>): ProxyableInspektion<T> = throw InspektNotIntrinsifiedException()