interface Test {
    fun test(): Int
}

fun box(): String {
    val proxy = proxy(Test::class) {
        4
    }

    @Suppress("USELESS_IS_CHECK")
    assertTrue(proxy is Test, "proxy is subtype of test")

    assertEquals(4, proxy.test(), "proxy method returns correct result call")

    return "OK"
}