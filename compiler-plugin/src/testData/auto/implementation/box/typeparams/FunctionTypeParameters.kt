class FunctionTypeParams {
    fun <T> nonRefied(r: T): T = r
    inline fun <reified R : Any> genericFunction(r: R): R = r
}

fun box(): String {
    val inspektion = inspekt(FunctionTypeParams::class)
    inspektion.function("nonRefied").apply {
        assertEquals("T", typeParameters[0].name)
        assertEquals(0, typeParameters[0].index)
        assertFalse(typeParameters[0].isReified)
    }



    inspektion.function("genericFunction").apply {
        assertEquals(1, typeParameters.size)

        assertEquals("R", typeParameters[0].name)
        assertEquals(0, typeParameters[0].index)
        assertTrue(typeParameters[0].isReified)
        assertEquals(1, typeParameters[0].erasedUpperBounds.size)
        assertEquals("Any", (typeParameters[0].erasedUpperBounds[0].classifier as? KClass<*>)?.simpleName)
    }

    return "OK"
}
