class Test {
    fun test(): Int = error("Test!")
}

fun box(): String {
    val spekt = assertSuccessful("inspekt(Test::class)") { inspekt(Test::class) }
    val e = assertFailsWith<Throwable> {
        spekt(Test(), "test")
    }

    assertTrue(e is IllegalStateException, "Expected IllegalStateException, got ${e}")
    assertEquals(e.message, "Test!")


    val e2 = assertFailsWith<Throwable> {
        spekt.function("test").invoke {
            value(1)
        }
    }
    assertTrue(e2 is FunctionInvocationException, "Expected FunctionInvocationException, got ${e2}")
    assertEquals("Failed to invoke function auto.implementation.box.invoke.Test.test", e2.message)
    assertTrue(e2.cause is IndexOutOfBoundsException, "Expected IndexOutOfBoundsException, got ${e2.cause}")
    assertEquals("Parameter index out of bounds: 0, number of parameters of kind VALUE is 0", e2.cause.message)


    val e3 = assertFailsWith<Throwable> {
        spekt.function("test").invoke {
        }
    }
    assertTrue(e3 is FunctionInvocationException, "Expected FunctionInvocationException, got ${e3}")
    assertEquals("Failed to invoke function auto.implementation.box.invoke.Test.test", e3.message)
    assertTrue(e3.cause is IllegalArgumentException, "Expected IllegalArgumentException, got ${e3.cause}")
    assertEquals("Argument for the dispatch receiver was null. This is not allowed.", e3.cause.message)

    return "OK"
}
