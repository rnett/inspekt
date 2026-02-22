interface MultiBound<T> where T : Any, T : Comparable<T>

fun box(): String {
    val inspektion = inspekt(MultiBound::class)
    val typeParam = inspektion.typeParameters[0]

    assertEquals("T", typeParam.name)
    assertEquals(2, typeParam.erasedUpperBounds.size)

    val boundClassNames = typeParam.erasedUpperBounds.map { (it.classifier as? KClass<*>)?.simpleName }.toSet()
    assertTrue("Any" in boundClassNames)
    assertTrue("Comparable" in boundClassNames)

    return "OK"
}
