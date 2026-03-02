//[inspekt](../../index.md)/[dev.rnett.inspekt.proxy](index.md)/[proxyFactory](proxy-factory.md)

# proxyFactory

[common]\

@[InspektCompilerPluginIntrinsic](../dev.rnett.inspekt.exceptions/-inspekt-compiler-plugin-intrinsic/index.md)

fun &lt;[T](proxy-factory.md) : [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)&gt; [proxyFactory](proxy-factory.md)(@[ReferenceLiteral](../../../inspekt/dev.rnett.inspekt.utils/-reference-literal/index.md)(mustBeInterface = true)toImplement: [KClass](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.reflect/-k-class/index.html)&lt;[T](proxy-factory.md)&gt;, @[StringLiteral](../../../inspekt/dev.rnett.inspekt.utils/-string-literal/index.md)name: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null): ([ProxyHandler](-proxy-handler/index.md)) -&gt; [T](proxy-factory.md)

Create a proxy object factory, for proxies that implements [T](proxy-factory.md) and responds to **all** method or accessor calls using the handler passed to the factory. [toImplement](proxy-factory.md)**must be a class reference literal of an interface**. The resulting object will implement [toImplement](proxy-factory.md).

A call to this method is transformed into an anonymous object instance by the compiler plugin. All the arguments are calculated at compilation time.

Because of this, uses of this function can potentially add a significant amount of binary size. This is based on the number of appearances of `proxyFactory()` in your code, not how many times it is invoked. **Calling the factory does not add overhead**, only the `proxyFactory` call.

#### Parameters

common

| | |
|---|---|
| name | the name to use for the proxy class. Must be a constant value. |

#### See also

| |
|---|
| [proxy](proxy.md) |

#### Throws

| | |
|---|---|
| [InspektNotIntrinsifiedException](../dev.rnett.inspekt.exceptions/-inspekt-not-intrinsified-exception/index.md) | if it was not intrinsified by the Inspekt compiler plugin. |
