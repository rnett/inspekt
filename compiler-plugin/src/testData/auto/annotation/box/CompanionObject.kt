@Inspekt
class Test {
    @Inspekt
    companion object {
        fun test() = 5
        val test2 = 6
    }
}

fun box(): String {
    return Test.inspekt().toString() + "\n\n\n" + Test.inspektCompanion().toString()
}