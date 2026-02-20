fun test() {

}

class Test

val testProp: Int = 34

fun impl() {
    val function = ::test
    val cls = Test::class
    val prop = ::testProp
    inspekt(<!INSPEKT_MUST_BE_REFERENCE_LITERAL!>function<!>)
    inspekt(<!INSPEKT_MUST_BE_REFERENCE_LITERAL!>cls<!>)
    inspekt(<!INSPEKT_MUST_BE_REFERENCE_LITERAL!>prop<!>)
}