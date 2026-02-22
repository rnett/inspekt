package dev.rnett.inspekt.exceptions

import dev.rnett.inspekt.model.Callable
import dev.rnett.inspekt.model.name.CallableName
import dev.rnett.inspekt.model.name.QualifiedName
import dev.rnett.inspekt.proxy.ProxyHandler
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Exception thrown when a function invocation fails.
 */
public class FunctionInvocationException(
    public val name: QualifiedName,
    public override val cause: Throwable
) : RuntimeException("Failed to invoke function $name", cause)

//TODO opt-out of stack trace
internal class UnderlyingFunExceptionWrapper(val wrapped: Throwable) : RuntimeException(wrapped)

//TODO opt-out of stack trace
internal class UnderlyingFunInvocationWrapper(val wrapped: Throwable) : RuntimeException(wrapped)


private fun unwrap(exception: Throwable): Throwable {
    return when (exception) {
        is UnderlyingFunInvocationWrapper -> unwrap(exception.wrapped)
        is FunctionInvocationException -> unwrap(exception.cause)
        is UnderlyingFunExceptionWrapper -> UnderlyingFunExceptionWrapper(unwrap(exception.wrapped))
        else -> exception
    }
}

@PublishedApi
internal fun handleException(name: CallableName, exception: Throwable): Nothing {
    val unwrapped = unwrap(exception)
    if (unwrapped is UnderlyingFunExceptionWrapper) {
        throw unwrapped.wrapped
    }
    throw FunctionInvocationException(name, unwrapped)
}

@PublishedApi
internal inline fun <R> Callable.wrapInvocation(block: () -> R): R {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
    try {
        return block()
    } catch (e: Throwable) {
        handleException(name, e)
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