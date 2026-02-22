interface Interdependent<T, U : List<T>>

fun box(): String {
    val inspektion = inspekt(Interdependent::class)
    val typeParams = inspektion.typeParameters

    assertEquals(2, typeParams.size)

    assertEquals("T", typeParams[0].name)
    assertEquals("U", typeParams[1].name)

    val uUpperBound = typeParams[1].erasedUpperBounds[0]
    assertEquals("List", (uUpperBound.classifier as? KClass<*>)?.simpleName)

    return "OK"
}
