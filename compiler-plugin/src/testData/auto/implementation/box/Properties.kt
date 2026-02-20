@Inspekt
class TestProperties {
    val readOnly = "fixed"
    var mutable = "initial"
    
    val withGetter get() = mutable + "_got"
    
    var withSetter = "initial"
        set(value) {
            field = value + "_set"
        }
    
    val delegated by lazy { "lazy_value" }
}

fun box(): String {
    val spekt = TestProperties.inspekt()
    val instance = TestProperties()
    
    val pReadOnly = spekt.properties.single { it.name.name == "readOnly" } as ReadOnlyProperty
    assertEquals("fixed", assertSuccessful("readOnly get") { pReadOnly.get { dispatchReceiver = instance } })
    
    val pMutable = spekt.properties.single { it.name.name == "mutable" } as MutableProperty
    assertEquals("initial", assertSuccessful("mutable get") { pMutable.get { dispatchReceiver = instance } })
    assertSuccessful("mutable set") {
        pMutable.set {
            dispatchReceiver = instance
            value("changed")
        }
    }
    assertEquals("changed", instance.mutable)
    assertEquals("changed", assertSuccessful("mutable get after set") { pMutable.get { dispatchReceiver = instance } })
    
    val pWithGetter = spekt.properties.single { it.name.name == "withGetter" } as ReadOnlyProperty
    assertEquals("changed_got", assertSuccessful("withGetter get") { pWithGetter.get { dispatchReceiver = instance } })
    
    val pWithSetter = spekt.properties.single { it.name.name == "withSetter" } as MutableProperty
    assertSuccessful("withSetter set") {
        pWithSetter.set {
            dispatchReceiver = instance
            value("new")
        }
    }
    assertEquals("new_set", instance.withSetter)
    assertEquals("new_set", assertSuccessful("withSetter get") { pWithSetter.get { dispatchReceiver = instance } })
    
    val pDelegated = spekt.properties.single { it.name.name == "delegated" } as ReadOnlyProperty
    assertEquals("lazy_value", assertSuccessful("delegated get") { pDelegated.get { dispatchReceiver = instance } })

    return "OK"
}
