interface VarianceTest<in I, out O, INVARIANT>

fun box(): String {
    val inspektion = inspekt(VarianceTest::class)
    val typeParams = inspektion.typeParameters

    assertEquals(3, typeParams.size)

    assertEquals("I", typeParams[0].name)
    assertEquals(TypeParameter.Variance.IN, typeParams[0].variance)

    assertEquals("O", typeParams[1].name)
    assertEquals(TypeParameter.Variance.OUT, typeParams[1].variance)

    assertEquals("INVARIANT", typeParams[2].name)
    assertEquals(TypeParameter.Variance.INVARIANT, typeParams[2].variance)

    return "OK"
}
