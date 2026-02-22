interface Base {
    fun test(a: Int = 1): String = "Base($a)"
}

fun box(): String {
    val proxy = proxy(Base::class) {
        callSuper()
    }

    assertEquals("Base(1)", proxy.test(), "Default value")
    assertEquals("Base(2)", proxy.test(2), "Provided value")

    return "OK"
}
