interface Test {
    fun test(a: Int): Int = a + 5
}

fun box(): String {
    val proxy = proxy(Test::class) {
        (callSuper() as Int) + 2
    }

    assertEquals(9, proxy.test(2), "proxy method returns correct result call")

    return "OK"
}