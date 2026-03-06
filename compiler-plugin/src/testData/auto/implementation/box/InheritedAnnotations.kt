package auto

import dev.rnett.inspekt.inspekt

annotation class Ann1(val value: String)
annotation class Ann2(val value: String)

interface Base {
    @Ann1("baseMethod")
    fun myMethod()

    @Ann2("baseProp")
    val myProp: String
}

open class Parent : Base {
    @Ann2("parentMethod")
    override fun myMethod() {
    }

    @Ann1("parentProp")
    override val myProp: String = "parent"
}

class Child : Parent() {
    @Ann1("childMethod")
    override fun myMethod() {
    }

    @Ann2("childProp")
    override val myProp: String = "child"
}

fun box(): String {
    val childInspektion = inspekt(Child::class)
    val childMethod = childInspektion.function("myMethod")

    val mAnns = childMethod.allAnnotations.map { it.annotation.toString() to it.source?.simpleName }.toSet()

    // Ann1("childMethod") -> null
    // Ann2("parentMethod") -> Parent
    // Ann1("baseMethod") -> Base
    if (mAnns.size != 3) return "Fail: myMethod annotations size is ${mAnns.size}"

    val expectedMethod = setOf(
        "@auto.Ann1(value=childMethod)" to null,
        "@auto.Ann2(value=parentMethod)" to "Parent",
        "@auto.Ann1(value=baseMethod)" to "Base"
    )

    if (mAnns != expectedMethod) return "Fail: myMethod annotations mismatch: $mAnns"

    val childProp = childInspektion.property("myProp")

    val pAnns = childProp.allAnnotations.map { it.annotation.toString() to it.source?.simpleName }.toSet()

    // Ann2("childProp") -> null
    // Ann1("parentProp") -> Parent
    // Ann2("baseProp") -> Base
    if (pAnns.size != 3) return "Fail: myProp annotations size is ${pAnns.size}"

    val expectedProp = setOf(
        "@auto.Ann2(value=childProp)" to null,
        "@auto.Ann1(value=parentProp)" to "Parent",
        "@auto.Ann2(value=baseProp)" to "Base"
    )

    if (pAnns != expectedProp) return "Fail: myProp annotations mismatch: $pAnns"

    return "OK"
}
