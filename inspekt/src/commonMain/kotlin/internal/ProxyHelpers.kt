package dev.rnett.inspekt.internal

import dev.rnett.inspekt.exceptions.ProxyInvocationException
import dev.rnett.inspekt.model.Inspektion
import dev.rnett.inspekt.model.Property
import dev.rnett.inspekt.model.SimpleFunction
import dev.rnett.inspekt.model.arguments.ArgumentList
import dev.rnett.inspekt.proxy.ProxyHandler
import dev.rnett.inspekt.proxy.ProxyableInspektion
import dev.rnett.inspekt.proxy.SuperCall
import dev.rnett.symbolexport.ExportSymbol

@Suppress("unused")
@ExportSymbol
@PublishedApi
internal fun v1ProxyHelper(
    @ExportSymbol functionName: String,
    @ExportSymbol proxyInstance: Any,
    @ExportSymbol handler: ProxyHandler,
    @ExportSymbol originalMethod: Any,
    @ExportSymbol originalProperty: Any?,
    @ExportSymbol isSetter: Boolean,
    @ExportSymbol args: Array<Any?>
): Any? {
    try {
        originalMethod as SimpleFunction
    } catch (e: ClassCastException) {
        throw ProxyInvocationException(functionName, handler, proxyInstance, IllegalArgumentException("Internal error: original function was not of SimpleFunction type", e))
    }
    try {
        originalProperty as Property?
    } catch (e: ClassCastException) {
        throw ProxyInvocationException(functionName, handler, proxyInstance, IllegalArgumentException("Internal error: original property was not of Property? type", e))
    }

    val argsList = try {
        ArgumentList(originalMethod.parameters, args.asList())
    } catch (e: Throwable) {
        throw ProxyInvocationException(functionName, handler, proxyInstance, e)
    }
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
    functionName: String,
    @ExportSymbol proxyInstance: Any,
    handler: ProxyHandler,
    originalMethod: Any,
    originalProperty: Any?,
    isSetter: Boolean,
    args: Array<Any?>
): Any? {
    try {
        originalMethod as SimpleFunction
    } catch (e: ClassCastException) {
        throw ProxyInvocationException(functionName, handler, proxyInstance, IllegalArgumentException("Internal error: original function was not of SimpleFunction type", e))
    }
    try {
        originalProperty as Property?
    } catch (e: ClassCastException) {
        throw ProxyInvocationException(functionName, handler, proxyInstance, IllegalArgumentException("Internal error: original property was not of Property? type", e))
    }

    val argsList = try {
        ArgumentList(originalMethod.parameters, args.asList())
    } catch (e: Throwable) {
        throw ProxyInvocationException(functionName, handler, proxyInstance, e)
    }
    val original = when {
        originalProperty == null -> SuperCall.FunctionCall(originalMethod, argsList)
        isSetter -> SuperCall.PropertySet(originalProperty, originalMethod.asSetter(originalProperty), argsList)
        else -> SuperCall.PropertyGet(originalProperty, originalMethod.asGetter(originalProperty), argsList)
    }
    return handler.run {
        original.handleSuspend()
    }
}

@Suppress("UNCHECKED_CAST", "unused")
@ExportSymbol
@PublishedApi
internal fun <T : Any> v1ProxyableSpektHelper(
    inspektion: Inspektion<*>,
    proxyFactory: (ProxyHandler) -> Any?
): ProxyableInspektion<T> = ProxyableInspektion(inspektion as Inspektion<T>, proxyFactory as (ProxyHandler) -> T)