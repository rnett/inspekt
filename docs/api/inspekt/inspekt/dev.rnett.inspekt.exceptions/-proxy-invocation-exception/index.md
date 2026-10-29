//[inspekt](../../../index.md)/[dev.rnett.inspekt.exceptions](../index.md)/[ProxyInvocationException](index.md)

# ProxyInvocationException

[common]\
class [ProxyInvocationException](index.md)(val name: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), val proxyHandler: [ProxyHandler](../../dev.rnett.inspekt.proxy/-proxy-handler/index.md), val proxyInstance: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html), cause: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html)) : [RuntimeException](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-runtime-exception/index.html)

Exception thrown when a function invocation on a proxy fails.

## Constructors

| | |
|---|---|
| [ProxyInvocationException](-proxy-invocation-exception.md) | [common]<br>constructor(name: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), proxyHandler: [ProxyHandler](../../dev.rnett.inspekt.proxy/-proxy-handler/index.md), proxyInstance: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html), cause: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [cause](../-inspekt-not-intrinsified-exception/index.md#-654012527%2FProperties%2F162778885) | [common]<br>expect open val [cause](../-inspekt-not-intrinsified-exception/index.md#-654012527%2FProperties%2F162778885): [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html)? |
| [message](../-inspekt-not-intrinsified-exception/index.md#1824300659%2FProperties%2F162778885) | [common]<br>expect open val [message](../-inspekt-not-intrinsified-exception/index.md#1824300659%2FProperties%2F162778885): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? |
| [name](name.md) | [common]<br>val [name](name.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |
| [proxyHandler](proxy-handler.md) | [common]<br>val [proxyHandler](proxy-handler.md): [ProxyHandler](../../dev.rnett.inspekt.proxy/-proxy-handler/index.md) |
| [proxyInstance](proxy-instance.md) | [common]<br>val [proxyInstance](proxy-instance.md): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html) |
