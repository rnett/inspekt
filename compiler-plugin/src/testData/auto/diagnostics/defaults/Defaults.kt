fun test(a: Int = 1, b: Int = 2, c: Int = 3, d: Int = 4, e: Int = 5, f: Int = 6) {

}

interface Test {
    fun test2(a: Int = 1, b: Int = 2, c: Int = 3, d: Int = 4, e: Int = 5, f: Int = 6) {

    }
}

fun impl() {
    inspekt(<!INSPEKT_DEFAULT_WARNING_test!>::test<!>)

    inspekt(<!INSPEKT_DEFAULT_WARNING_test2!>Test::class<!>)

    proxy(<!INSPEKT_DEFAULT_WARNING_test2!>Test::class<!>) {}
}