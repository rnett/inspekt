@Inspekt
object TestObject {
    val prop = "obj_prop"
    fun test() = "obj_fun"
}

fun box(): String {
    // Object
    val spektObj = TestObject.spekt()
    assertEquals(TestObject, spektObj.objectInstance)
    
    val pObj = spektObj.properties.single { it.name.name == "prop" }
    assertEquals("obj_prop", assertSuccessful("prop get") { pObj.get { dispatchReceiver = TestObject } })
    
    val fObj = spektObj.functions.single { it.name.name == "test" }
    assertEquals("obj_fun", assertSuccessful("test invoke") { fObj.invoke { dispatchReceiver = TestObject } })

    return "OK"
}
