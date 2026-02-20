import dev.rnett.inspekt.proxy.proxy

interface Interface1 {
    fun test(): String = "1"
}

interface Interface2 {
    fun test(): String = "2"
}

fun box(): String {
    // This should fail compilation with "Conflicting methods found in superinterfaces"
    val proxy = proxy(Interface1::class, Interface2::class) {
        "OK"
    }

    return proxy.test()
}
