interface Base {
    var prop: String
        get() = "BaseGetter"
        set(value) {}
}

interface Intermediate : Base {
    override var prop: String
        get() = super.prop
        set(value) {
            // some logic
        }
}

fun box(): String {
    val proxy = proxy(Intermediate::class) {
        callSuper()
    }

    assertEquals("BaseGetter", proxy.prop, "Intermediate getter (delegates to Base)")
    // We can't easily verify the setter logic without more state, but this tests that it's correctly overridden and callable
    proxy.prop = "something"

    return "OK"
}
