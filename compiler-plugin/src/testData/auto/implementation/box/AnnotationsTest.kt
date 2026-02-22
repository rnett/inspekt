annotation class TestAnnotation(val a: Int)

@TestAnnotation(1)
class Test {
    @TestAnnotation(2)
    @ExternalAnnotation("test", List::class)
    val prop: Int = 2

    fun test(@TestAnnotation(3) a: Int = 3) = ""
}

fun box(): String {
    val spekt = inspekt(Test::class)

    assertEquals(TestAnnotation(1), spekt.annotation<TestAnnotation>(), "Test annotation should be present")

    val prop = spekt.properties.single()
    assertEquals(TestAnnotation(2), prop.annotation<TestAnnotation>(), "Property annotation should be present")

    assertEquals(ExternalAnnotation("test", List::class), prop.annotation<ExternalAnnotation>(), "External annotation should be present")

    val test = spekt.declaredFunctions.single()
    val testA = test.parameters.last()

    assertEquals(TestAnnotation(3), testA.annotation<TestAnnotation>(), "Parameter annotation should be present")

    return "OK"
}