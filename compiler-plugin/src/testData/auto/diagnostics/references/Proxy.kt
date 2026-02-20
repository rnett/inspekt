interface Test2

class Test

fun impl() {
    val int = Test2::class

    proxy(<!INSPEKT_MUST_BE_REFERENCE_LITERAL!>int<!>) {}
    proxy(<!INSPEKT_MUST_BE_INTERFACE!>Test::class<!>) {}

    proxy(Test2::class, <!INSPEKT_MUST_BE_REFERENCE_LITERAL!>int<!>) {}
    proxy(Test2::class, <!INSPEKT_MUST_BE_INTERFACE!>Test::class<!>) {}

    proxy(Test2::class, Test2::class, <!INSPEKT_MUST_BE_REFERENCE_LITERAL!>int<!>) {}
    proxy(Test2::class, Test2::class, <!INSPEKT_MUST_BE_INTERFACE!>Test::class<!>) {}

    proxyFactory(<!INSPEKT_MUST_BE_REFERENCE_LITERAL!>int<!>)
    proxyFactory(<!INSPEKT_MUST_BE_INTERFACE!>Test::class<!>)

    proxyFactory(Test2::class, <!INSPEKT_MUST_BE_REFERENCE_LITERAL!>int<!>)
    proxyFactory(Test2::class, <!INSPEKT_MUST_BE_INTERFACE!>Test::class<!>)

    proxyFactory(Test2::class, Test2::class, <!INSPEKT_MUST_BE_REFERENCE_LITERAL!>int<!>)
    proxyFactory(Test2::class, Test2::class, <!INSPEKT_MUST_BE_INTERFACE!>Test::class<!>)

    inspektAndProxy(<!INSPEKT_MUST_BE_REFERENCE_LITERAL!>int<!>)
    inspektAndProxy(<!INSPEKT_MUST_BE_INTERFACE!>Test::class<!>)
}