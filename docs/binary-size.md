# Binary Size

Unlike traditional reflection, which relies on metadata already present in your compiled code, Inspekt generates the necessary reflection information at compile-time.
This is what makes Kotlin Multiplatform support possible, but it also means that the more you use `inspekt()`, the larger your binary becomes.
This is true of all of Inspekt's intrinsic methods: `inspekt`, `proxy`, `proxyFactory`, etc.

See [How it works](how-it-works.md) for details on the transformations done by the compiler plugin.

## What Contributes to Binary Size?

Each call site of `inspekt()` function causes the compiler plugin to generate new code and metadata for the inspected declaration. This includes:

* **Member Metadata:** Information about properties, functions, parameters, and annotations.
* **Invokers:** Specialized code that allows Inspekt to call your functions and get/set properties efficiently.
* **Type Information:** Details about type parameters, generic types, and class hierarchies.

[//]: # (@formatter:off)
!!! important "Call sites vs calls"
    The size added by `inspekt()`, `proxy()`, and other intrinsics, is determined by the number of call sites compiled, i.e. the number of `inspekt()`s in your code.
    How many times that call is executed has no effect - `inspekt(Foo::class)` adds the same amount of binary size as `repeat(1000) { inspekt(Foo::class) }`.

[//]: # (@formatter:on)

The total impact on your binary size is determined by two main factors:

1. **Number of `inspekt()` calls:** Each **call site** of `inspekt()` is essentially a new "instance" of reflection metadata in your compiled code.
2. **Complexity of the inspected declaration:** A large class with many members will result in more generated metadata than a small class or a single function.

## Managing Binary Size

Here are several strategies for keeping your binary size under control:

### Reuse Inspektion Objects

The most effective way to minimize binary size is to have as few `inspekt()` call sites as possible. If you need to inspect the same class in multiple places, store the `Inspektion` object in a property or a singleton and reuse it.

```kotlin
// Good: Call once and reuse
object MyReflection {
    val fooInspektion = inspekt(Foo::class)
}

// Bad: Each call adds to the binary
fun doSomething() {
    val foo = inspekt(Foo::class)
}
```

Because the call itself is replaced by the compiler plugin, using a helper method will also work fine:

```kotlin
fun getFooInspektion() = inspekt(Foo::class)
```

But you would still needlessly re-create the object each time.

#### Proxies

Like `inspekt()`, each call site of `proxy()` adds to the binary size.
Unlike `inspekt()`, you can't really store the result of calling `proxy()`, because it depends on the handler.

To avoid adding binary size, wrap repeated proxy calls for the same interface in a helper function:

```kotlin
fun createFooProxy(handler: ProxyHandler) = proxy(Foo::class, handler)
```

Or just use `proxyFactory`, which does that for you.