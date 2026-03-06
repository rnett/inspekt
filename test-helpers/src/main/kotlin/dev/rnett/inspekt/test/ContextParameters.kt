package dev.rnett.inspekt.test

class MyContext

interface ExternalBase {
    context(ctx: MyContext)
    fun foo()
}
