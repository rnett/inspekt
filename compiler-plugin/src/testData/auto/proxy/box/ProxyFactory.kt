interface TestFactory {
    fun test(): Int
}

fun box(): String {
    val factory = proxyFactory(TestFactory::class)
    val proxy = factory {
        4
    }

    @Suppress("USELESS_IS_CHECK")
    assertTrue(proxy is TestFactory, "proxy is subtype of TestFactory")

    assertEquals(4, proxy.test(), "proxy method returns correct result call")

    return "OK"
}
