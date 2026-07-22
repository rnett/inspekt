//[inspekt](../../index.md)/[dev.rnett.inspekt.proxy](index.md)/[inspektAndProxy](inspekt-and-proxy.md)

# inspektAndProxy

[common]\

@[InspektCompilerPluginIntrinsic](../dev.rnett.inspekt.exceptions/-inspekt-compiler-plugin-intrinsic/index.md)

fun &lt;[T](inspekt-and-proxy.md) : [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)&gt; [inspektAndProxy](inspekt-and-proxy.md)(@[ReferenceLiteral](../../../inspekt/dev.rnett.inspekt.utils/-reference-literal/index.md)(mustBeInterface = true)toImplement: [KClass](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.reflect/-k-class/index.html)&lt;[T](inspekt-and-proxy.md)&gt;, @[StringLiteral](../../../inspekt/dev.rnett.inspekt.utils/-string-literal/index.md)name: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? = null): [ProxyableInspektion](-proxyable-inspektion/index.md)&lt;[T](inspekt-and-proxy.md)&gt;

Create an [Inspektion](../dev.rnett.inspekt.model/-inspektion/index.md) and a proxy object factory, for proxies that implements [T](inspekt-and-proxy.md) and responds to **all** method or accessor calls using the handler passed to the factory. [toImplement](inspekt-and-proxy.md)**must be a class reference literal of an interface**.

A call to this method is transformed into an anonymous object instance by the compiler plugin. All the arguments are calculated at compilation time.

Because of this, uses of this function can potentially add a significant amount of binary size. This is based on the number of appearances of `inspektAndProxy()` in your code, not how many times it is invoked. **Calling the factory does not add overhead**, only the `inspektAndProxy` call.

#### Parameters

common

| | |
|---|---|
| name | the name to use for the proxy class. Must be a constant value. |

#### See also

| |
|---|
| [inspekt](../dev.rnett.inspekt/inspekt.md) |
| [proxyFactory](proxy-factory.md) |

#### Throws

| | |
|---|---|
| [InspektNotIntrinsifiedException](../dev.rnett.inspekt.exceptions/-inspekt-not-intrinsified-exception/index.md) | if it was not intrinsified by the Inspekt compiler plugin. |
