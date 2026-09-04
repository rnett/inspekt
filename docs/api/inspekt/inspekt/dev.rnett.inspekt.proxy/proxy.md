//[inspekt](../../index.md)/[dev.rnett.inspekt.proxy](index.md)/[proxy](proxy.md)

# proxy

[common]\

@[InspektCompilerPluginIntrinsic](../dev.rnett.inspekt.exceptions/-inspekt-compiler-plugin-intrinsic/index.md)

fun &lt;[T](proxy.md) : [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)&gt; [proxy](proxy.md)(@[ReferenceLiteral](../../../inspekt/dev.rnett.inspekt.utils/-reference-literal/index.md)(mustBeInterface = true)toImplement: [KClass](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.reflect/-k-class/index.html)&lt;[T](proxy.md)&gt;, @[StringLiteral](../../../inspekt/dev.rnett.inspekt.utils/-string-literal/index.md)name: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null, handler: [ProxyHandler](-proxy-handler/index.md)): [T](proxy.md)

Create a proxy object that implements [T](proxy.md) and responds to **all** method or accessor calls using [handler](proxy.md). [toImplement](proxy.md)**must be a class reference literal of an interface**. The resulting object will implement [toImplement](proxy.md).

A call to this method is transformed into an anonymous object instance by the compiler plugin. All the arguments are calculated at compilation time.

Because of this, uses of this function can potentially add a significant amount of binary size. This is based on the number of appearances of `proxy()` in your code, not how many times it is invoked. If you find yourself repeatedly creating proxies for the same class, consider using [proxyFactory](proxy-factory.md), which has a constant binary overhead per factory invocation.

#### Parameters

common

| | |
|---|---|
| name | the name to use for the proxy class. Must be a constant value. |

#### Throws

| | |
|---|---|
| [InspektNotIntrinsifiedException](../dev.rnett.inspekt.exceptions/-inspekt-not-intrinsified-exception/index.md) | if it was not intrinsified by the Inspekt compiler plugin. |
