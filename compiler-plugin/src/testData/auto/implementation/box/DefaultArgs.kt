@Inspekt
class TestDefaults(val a: Int = 1, val b: String = "default") {
    fun test(c: Int = 3, d: String = "fun-default"): String {
        return "$a-$b-$c-$d"
    }
}

fun box(): String {
    val spekt = TestDefaults.inspekt()
    
    // Test constructor defaults
    val constructor = spekt.primaryConstructor!!
    
    val instance1 = assertSuccessful("constructor all defaults") { 
        constructor.invoke { 
        // all defaults
        }
    } as TestDefaults
    assertEquals("1-default-3-fun-default", instance1.test())
    
    val instance2 = assertSuccessful("constructor override a") {
        constructor.invoke {
        this["a"] = 10
        }
    } as TestDefaults
    assertEquals("10-default-3-fun-default", instance2.test())
    
    val instance3 = assertSuccessful("constructor override a & b") {
        constructor.invoke {
        this["a"] = 20
        this["b"] = "provided"
        }
    } as TestDefaults
    assertEquals("20-provided-3-fun-default", instance3.test())
    
    // Test function defaults
    val testMethod = spekt.functions.single { it.name.name == "test" }
    
    assertEquals("20-provided-3-fun-default", assertSuccessful("method defaults with only dispatch receiver") {
        testMethod.invoke {
        dispatchReceiver = instance3
        }
    })
    
    assertEquals("20-provided-100-fun-default", assertSuccessful("method override c") {
        testMethod.invoke {
        dispatchReceiver = instance3
        this["c"] = 100
        }
    })
    
    assertEquals("20-provided-100-manual", assertSuccessful("method override c & d") {
        testMethod.invoke {
        dispatchReceiver = instance3
        this["c"] = 100
        this["d"] = "manual"
        }
    })

    return "OK"
}
