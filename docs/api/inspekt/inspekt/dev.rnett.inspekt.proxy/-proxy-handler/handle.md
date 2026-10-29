//[inspekt](../../../index.md)/[dev.rnett.inspekt.proxy](../index.md)/[ProxyHandler](index.md)/[handle](handle.md)

# handle

[common]\
abstract fun [SuperCall](../-super-call/index.md).[handle](handle.md)(): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?

Handle a call to this proxy.

If you call super, make sure you check [SuperCall.isSuperCallable](../-super-call/is-super-callable.md) and [SuperCall.isSuspending](../-super-call/is-suspending.md) first. Calls to abstract super methods will throw [FunctionInvocationException](../../dev.rnett.inspekt.exceptions/-function-invocation-exception/index.md).
