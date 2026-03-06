// LANGUAGE: +ContextParameters

import dev.rnett.inspekt.test.ExternalBase
import dev.rnett.inspekt.test.MyContext

object Impl : ExternalBase {
    context(ctx: MyContext)
    override fun foo() {
    }
}

fun box(): String {
    val inspektion = inspekt(Impl::class)
    val foo = inspektion.functions.first { it.name.name == "foo" }
    val result = foo.parameters.joinToString { "${it.name}: ${it.kind}" }
    val expected = "<this>: DISPATCH, ctx: CONTEXT"
    return if (result == expected) "OK" else "Actual: $result"
}
