package dev.rnett.inspekt

import dev.rnett.inspekt.proxy.ProxyHandler
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Exception thrown when a function invocation fails.
 */
@Suppress("CanBeParameter")
public class FunctionInvocationException(
    public val name: QualifiedName,
    cause: Throwable
) : RuntimeException("Failed to invoke function $name", cause)

//TODO don't wrap exceptions thrown by the actual function
@PublishedApi
internal inline fun <R> Callable.wrapInvocation(block: () -> R): R {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
    try {
        return block()
    } catch (e: Throwable) {
        if (e is FunctionInvocationException) throw e
        throw FunctionInvocationException(name, e)
    }
}

/**
 * Exception thrown when a function invocation on a proxy fails.
 */
@Suppress("CanBeParameter")
public class ProxyInvocationException(
    public val name: String,
    public val proxyHandler: ProxyHandler,
    public val proxyInstance: Any,
    cause: Throwable
) : RuntimeException("Failed to invoke function $name on proxy $proxyInstance with proxy handler $proxyHandler", cause)