@Inspekt
class Test(val test1: String = "test") {
    fun test() = 5
    val test2 = 6

    companion object {
        val t by lazy {
            5
        }
    }
}

fun box(): String {
    return Test.spekt().toString()
}