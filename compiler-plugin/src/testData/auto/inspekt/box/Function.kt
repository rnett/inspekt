class Test(val test1: String = "test") {
    fun test() = 5
    val test2 = 6
}

fun box(): String {
    return inspekt(Test::test).toString()
}