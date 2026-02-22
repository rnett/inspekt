class Test {
    suspend fun test() = 5
}

fun box(): String {
    val spekt = assertSuccessful("inspekt(Test::class)") { inspekt(Test::class) }
    val suspendFun = spekt.functions.single { it.name.name == "test" }
    assertEquals(true, suspendFun.isSuspend)
    
    val test = Test()
    
    runTest {
        assertEquals(5, assertSuccessful("suspend function invokeSuspend") {
            suspendFun.invokeSuspend {
                this[0] = test
            }
        })
    }
    return "OK"
}
