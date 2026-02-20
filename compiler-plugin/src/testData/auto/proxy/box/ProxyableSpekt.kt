interface TestProxyable {
    fun test(): Int
}

fun box(): String {
    val proxyable = inspektAndProxy(TestProxyable::class)

    assertEquals("TestProxyable", proxyable.inspektion.shortName)

    val proxy = proxyable.createProxy {
        4
    }

    @Suppress("USELESS_IS_CHECK")
    assertTrue(proxy is TestProxyable, "proxy is subtype of TestProxyable")

    assertEquals(4, proxy.test(), "proxy method returns correct result call")

    assertEquals(4, proxyable.inspektion.function("test").invoke {
        dispatchReceiver = proxy
    } as Int, "inspektion called method on proxy returns correct result")

    return "OK"
}
