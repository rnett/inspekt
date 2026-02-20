import dev.rnett.inspekt.proxy.proxy

interface Interface1 {
    fun test1(): String = "1"
}

interface Interface2 {
    fun test2(): String = "2"
}

fun box(): String {
    val proxy = proxy(Interface1::class, Interface2::class) {
        callSuper() as String
    }

    assertIs<Interface1>(proxy)
    assertIs<Interface2>(proxy)

    assertEquals("1", proxy.test1(), "Interface1.test1()")
    assertEquals("2", (proxy as Interface2).test2(), "Interface2.test2()")

    return "OK"
}
