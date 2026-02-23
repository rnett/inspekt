# How It Works

Inspekt is powered by a Kotlin compiler plugin that replaces `inspekt` and `proxy` calls with their results.
It works by generating the necessary metadata and code at compile-time, allowing you to use it on any Kotlin platform, including Kotlin/JS and Kotlin/Native.
This also makes it compatible with GraalVM, proguard, etc, without any additional configuration.

## The Compiler Plugin

The core of Inspekt is a Kotlin compiler plugin.
When the plugin is applied to your project, replaces any calls to `inspekt()`, `proxy()`, and the other intrinsic functions.

### `inspekt`

`inspekt()` calls are replaced by the constructor for the returned type, e.g. `Inspektion` or `SimpleFunction`.
The target is inspected, and its metadata, and that of any child declarations, is used to fill in the object's constructor arguments (for example, with the declaration's fully qualified name, or function parameters).
For functions, invoker lambdas are generated that call the function with arguments extracted from the passed `ArgumentsList`.

For example, a call like this:

```kotlin
class Foo(val bar: Int) {
    fun baz(): String = bar.toString()
}

val test = inspekt(Foo::class)
```

will be turned into something like this:

```kotlin
class Foo(val bar: Int) {
    fun baz(): String = bar.toString()
}

val test = Inspektion(
    kotlin = Foo::class,
    name = "Foo",
    superTypes = listOf(typeOf<Any>()),
    annotations = emptyList(),
    objectInstance = null,
    constructors = listOf(
        Constructor(
            isPrimary = true,
            parameters = listOf(Parameter(name = "bar", type = typeOf<Int>(), index = 0)),
            invoker = { args ->
                Foo(args[0] as Int)
            }
        )
    ),
    properties = listOf(),
    functions = listOf(
        Function(
            name = "baz",
            returnType = typeOf<String>(),
            parameters = listOf(Parameter(name = "this", kind = Kind.DISPATCH, type = typeOf<Foo>(), index = 0)),
            invoker = { args ->
                (args[0] as Foo).baz()
            }
        )
    )
)
```

### `proxy`

`proxy()` calls are replaced by an anonymous object that implements the target interface.
Every method in the interface is overridden by an implementation that assembles its arguments into an `ArgumentsList` and calls the proxy handler, returning the result.
Inspektions are also generated for all of the interface's members and used to populate the `SuperCall` object passed to the handler.

`proxyFactory()` and `inspektAndProxy` are very similar, they just wrap the anonymous object creation in a lambda that takes the handler instance.

For example, a call like:

```kotlin
interface Foo {
    fun bar(a: Int): Int = a + 2
    fun baz(): String
}

val test = proxy(Foo::class) {
    if (functionName == "bar") {
        return callSuper()
    } else if (functionName == "baz") {
        return "test"
    }
    error("Unrecognized function $functionName!")
}
```

will be turned into something like this:

```kotlin
interface Foo {
    fun bar(a: Int): Int = a + 2
    fun baz(): String
}

val test = object : Foo {
    private val handler: ProxyHandler = {
        if (functionName == "bar") {
            return callSuper()
        } else if (functionName == "baz") {
            return "test"
        }
        error("Unrecognized function $functionName!")
    }

    private val barInspektion = inspekt(Foo::bar)

    override fun bar(a: Int): Int {
        val args = ArgumentList(this, a)
        return with(handler) { SuperCall(barInspektion, args).handle() }
    }

    private val bazInspektion = inspekt(Foo::baz)
    override fun baz(): String {
        val args = ArgumentList(this)
        return with(handler) { SuperCall(bazInspektion, args).handle() }
    }
}
```