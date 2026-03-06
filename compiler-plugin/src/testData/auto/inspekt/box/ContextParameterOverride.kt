// LANGUAGE: +ContextParameters

class MyContext
class OtherContext

interface Base {
    context(ctx1: MyContext, ctx2: OtherContext)
    fun foo()
}

object Impl : Base {
    context(ctx1: MyContext, ctx2: OtherContext)
    override fun foo() {
    }
}

fun box(): String {
    val inspektion = inspekt(Impl::class)
    val foo = inspektion.functions.first { it.name.name == "foo" }
    val result = foo.parameters.joinToString { "${it.name}: ${it.kind}" }
    val expected = "<this>: DISPATCH, ctx1: CONTEXT, ctx2: CONTEXT"
    return if (result == expected) "OK" else "Actual: $result"
}
