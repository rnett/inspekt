@Inspekt
object Test {
    fun test() = 5
    val test2 = 6
}

fun box(): String {
    return Test.inspekt().toString()
}