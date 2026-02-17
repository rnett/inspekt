
@InSpekt
abstract class Base {
    abstract val abstractProp: String
    open val openProp = "base_open"
    val baseProp = "base"
    
    abstract fun abstractFun(): String
    open fun openFun() = "base_open_fun"
    fun baseFun() = "base_fun"
}

@InSpekt
class Derived : Base() {
    override val abstractProp = "derived_abstract"
    override val openProp = "derived_open"
    
    override fun abstractFun() = "derived_abstract_fun"
    override fun openFun() = "derived_open_fun"
    
    fun derivedFun() = "derived_fun"
}

fun box(): String {
    val spekt = Derived.spekt()
    val instance = Derived()
    
    // Properties
    val pAbstract = spekt.properties.single { it.name.name == "abstractProp" }
    assertEquals("derived_abstract", assertSuccessful("abstractProp get") { pAbstract.get { dispatchReceiver = instance } })
    assertEquals(null, pAbstract.inheritedFrom)
    
    val pOpen = spekt.properties.single { it.name.name == "openProp" }
    assertEquals("derived_open", assertSuccessful("openProp get") { pOpen.get { dispatchReceiver = instance } })
    assertEquals(null, pOpen.inheritedFrom)
    
    val pBase = spekt.properties.single { it.name.name == "baseProp" }
    assertEquals("base", assertSuccessful("baseProp get") { pBase.get { dispatchReceiver = instance } })
    assertEquals(Base::class, pBase.inheritedFrom)
    
    // Functions
    val fAbstract = spekt.functions.single { it.name.name == "abstractFun" }
    assertEquals("derived_abstract_fun", assertSuccessful("abstractFun invoke") { fAbstract.invoke { dispatchReceiver = instance } })
    assertEquals(null, fAbstract.inheritedFrom)
    
    val fOpen = spekt.functions.single { it.name.name == "openFun" }
    assertEquals("derived_open_fun", assertSuccessful("openFun invoke") { fOpen.invoke { dispatchReceiver = instance } })
    assertEquals(null, fOpen.inheritedFrom)
    
    val fBase = spekt.functions.single { it.name.name == "baseFun" }
    assertEquals("base_fun", assertSuccessful("baseFun invoke") { fBase.invoke { dispatchReceiver = instance } })
    assertEquals(Base::class, fBase.inheritedFrom)
    
    val fDerived = spekt.functions.single { it.name.name == "derivedFun" }
    assertEquals("derived_fun", assertSuccessful("derivedFun invoke") { fDerived.invoke { dispatchReceiver = instance } })
    assertEquals(null, fDerived.inheritedFrom)

    return "OK"
}
