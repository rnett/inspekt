package dev.rnett.inspekt

import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Exception thrown when a function invocation fails.
 */
public class InvocationFailureException(name: QualifiedName, cause: Throwable) : RuntimeException("Failed to invoke function $name", cause)

@PublishedApi
internal inline fun <R> Callable.wrapInvocation(block: () -> R): R {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
    try {
        return block()
    } catch (e: Throwable) {
        if (e is InvocationFailureException) throw e
        throw InvocationFailureException(name, e)
    }
}