interface Base {
    val prop: String
}

interface Sub : Base {
    override var prop: String
}

fun box(): String {

    var backing = "initial"

    val proxy = proxy(Sub::class) {
        if (superFun is dev.rnett.inspekt.model.PropertySetter) {
            backing = args[1] as String
            null
        } else {
            backing
        }
    }

    assertEquals("initial", proxy.prop, "initial value")
    proxy.prop = "updated"
    assertEquals("updated", proxy.prop, "updated value")

    return "OK"
}
