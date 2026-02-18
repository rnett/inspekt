package dev.rnett.spekt

import kotlin.reflect.KClass

/**
 * Assert that [block] does not throw and return its value.
 *
 * Adds [message] to the failure to identify which case failed in compiler-plugin tests.
 */
@Suppress("unused")
inline fun <T> assertSuccessful(message: String, block: () -> T): T {
    return try {
        block()
    } catch (t: Throwable) {
        throw AssertionError("$message: ${t::class.qualifiedName}: ${t.message}", t)
    }
}

@Suppress("unused")
class ExternalTest(val a: Int, val b: String = "test") : AutoCloseable {
    var testProp: Int = 0

    fun test(c: Int, d: String = "test") {}

    override fun close() {

    }
}

@Suppress("unused")
annotation class ExternalAnnotation(val name: String, val type: KClass<*>)