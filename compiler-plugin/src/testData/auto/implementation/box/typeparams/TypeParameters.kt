class GenericClass<T, R> {
    fun test(): T = TODO()
}

fun box(): String {
    val inspektion = inspekt(GenericClass::class)
    val typeParams = inspektion.typeParameters

    assertEquals(2, typeParams.size)

    assertEquals("T", typeParams[0].name)
    assertEquals(0, typeParams[0].index)
    assertFalse(typeParams[0].isReified)
    assertEquals(TypeParameter.Variance.INVARIANT, typeParams[0].variance)

    assertEquals("R", typeParams[1].name)
    assertEquals(1, typeParams[1].index)
    assertFalse(typeParams[1].isReified)
    assertEquals(TypeParameter.Variance.INVARIANT, typeParams[1].variance)

    return "OK"
}
