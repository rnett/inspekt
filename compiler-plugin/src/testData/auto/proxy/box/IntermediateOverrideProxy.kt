import dev.rnett.inspekt.proxy.proxy

interface Base {
    fun test(): String = "Base"
}

interface Intermediate : Base {
    override fun test(): String = "Intermediate"
}

fun box(): String {
    val proxy = proxy(Intermediate::class) {
        callSuper() as String
    }

    assertEquals("Intermediate", proxy.test(), "Intermediate.test()")

    return "OK"
}
