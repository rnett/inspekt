interface TestProxyable {
    fun test(): Int
}

fun box(): String {
    val proxyable = proxyableSpekt(TestProxyable::class)

    assertEquals("TestProxyable", proxyable.spekt.shortName)

    val proxy = proxyable.proxy {
        4
    }

    @Suppress("USELESS_IS_CHECK")
    assertTrue(proxy is TestProxyable, "proxy is subtype of TestProxyable")

    assertEquals(4, proxy.test(), "proxy method returns correct result call")

    assertEquals(4, proxyable.spekt.function("test").invoke {
        dispatchReceiver = proxy
    } as Int, "spekt called method on proxy returns correct result")

    return "OK"
}
