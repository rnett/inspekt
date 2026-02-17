package dev.rnett.spekt

/**
 * Assert that [block] does not throw and return its value.
 *
 * Adds [message] to the failure to identify which case failed in compiler-plugin tests.
 */
inline fun <T> assertSuccessful(message: String, block: () -> T): T {
    return try {
        block()
    } catch (t: Throwable) {
        throw AssertionError("$message: ${t::class.qualifiedName}: ${t.message}", t)
    }
}
