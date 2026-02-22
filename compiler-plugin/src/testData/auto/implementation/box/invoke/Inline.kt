@Suppress("NOTHING_TO_INLINE")
class Test {
    inline fun test() = 5
    inline fun <reified R : Comparable<R>> testReified() = 6
}

fun box(): String {

    val cls = inspekt(Test::class)
    val inst = Test()

    assertEquals(
        5,
        cls.function("test").invoke {
            dispatchReceiver = Test()
        } as Int,
        "inline invocation"
    )

    assertFalse(cls.function("testReified").isCallable)

    return "OK"
}