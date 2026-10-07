//[inspekt](../../../index.md)/[dev.rnett.inspekt.proxy](../index.md)/[ProxyableInspektion](index.md)

# ProxyableInspektion

[common]\
class [ProxyableInspektion](index.md)&lt;[T](index.md) : [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)&gt;(val inspektion: [Inspektion](../../dev.rnett.inspekt.model/-inspektion/index.md)&lt;[T](index.md)&gt;, factory: ([ProxyHandler](../-proxy-handler/index.md)) -&gt; [T](index.md))

An [Inspektion](../../dev.rnett.inspekt.model/-inspektion/index.md) of a class, and a method for creating proxies of it.

## Constructors

| | |
|---|---|
| [ProxyableInspektion](-proxyable-inspektion.md) | [common]<br>constructor(inspektion: [Inspektion](../../dev.rnett.inspekt.model/-inspektion/index.md)&lt;[T](index.md)&gt;, factory: ([ProxyHandler](../-proxy-handler/index.md)) -&gt; [T](index.md)) |

## Properties

| Name | Summary |
|---|---|
| [inspektion](inspektion.md) | [common]<br>val [inspektion](inspektion.md): [Inspektion](../../dev.rnett.inspekt.model/-inspektion/index.md)&lt;[T](index.md)&gt; |

## Functions

| Name | Summary |
|---|---|
| [createProxy](create-proxy.md) | [common]<br>fun [createProxy](create-proxy.md)(handler: [ProxyHandler](../-proxy-handler/index.md)): [T](index.md) |
