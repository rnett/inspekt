@Inspekt
class Test(val test1: String = "test") {
    fun test() = 5
    val test2 = 6
}

fun box(): String {
    assertTrue(inspekt(Test::class) == Test.inspekt())

    return "OK"
}