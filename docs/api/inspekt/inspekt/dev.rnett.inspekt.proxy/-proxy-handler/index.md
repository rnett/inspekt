//[inspekt](../../../index.md)/[dev.rnett.inspekt.proxy](../index.md)/[ProxyHandler](index.md)

# ProxyHandler

[common]\
fun interface [ProxyHandler](index.md)

A handler for calls to Inspekt proxies. Will be called for all method or property accessors on the proxy object.

Any calls to functions, or access of properties, will result in a call to [handle](handle.md), or [handleSuspend](handle-suspend.md) for suspending functions. By default, [handleSuspend](handle-suspend.md) simply calls [handle](handle.md).

## Functions

| Name | Summary |
|---|---|
| [handle](handle.md) | [common]<br>abstract fun [SuperCall](../-super-call/index.md).[handle](handle.md)(): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?<br>Handle a call to this proxy. |
| [handleSuspend](handle-suspend.md) | [common]<br>open suspend fun [SuperCall](../-super-call/index.md).[handleSuspend](handle-suspend.md)(): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?<br>Handle a suspending call to this proxy. Defaults to calling [handle](handle.md). |
