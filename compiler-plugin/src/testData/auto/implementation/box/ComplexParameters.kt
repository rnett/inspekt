
@InSpekt
class Complex {
    fun String.extension(other: Int) = "$this-$other"
    
    fun multi(a: Int, b: String, c: Double) = "$a-$b-$c"
}

fun box(): String {
    val spekt = Complex.spekt()
    val instance = Complex()
    
    val fExtension = spekt.functions.single { it.name.name == "extension" }
    assertEquals("test-123", assertSuccessful("extension function invoke") {
        fExtension.invoke {
            dispatchReceiver = instance
            extensionReceiver = "test"
            value(123)
        }
    })
    
    val fMulti = spekt.functions.single { it.name.name == "multi" }
    assertEquals("1-two-3.0", assertSuccessful("multi parameter function invoke") {
        fMulti.invoke {
            dispatchReceiver = instance
            value(1, "two", 3.0)
        }
    })

    return "OK"
}
