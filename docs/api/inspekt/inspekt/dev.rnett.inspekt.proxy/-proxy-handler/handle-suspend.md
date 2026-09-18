//[inspekt](../../../index.md)/[dev.rnett.inspekt.proxy](../index.md)/[ProxyHandler](index.md)/[handleSuspend](handle-suspend.md)

# handleSuspend

[common]\
open suspend fun [SuperCall](../-super-call/index.md).[handleSuspend](handle-suspend.md)(): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?

Handle a suspending call to this proxy. Defaults to calling [handle](handle.md).

If you call super, make sure you check [SuperCall.isSuperCallable](../-super-call/is-super-callable.md) first. Calls to abstract super methods will throw [FunctionInvocationException](../../dev.rnett.inspekt.exceptions/-function-invocation-exception/index.md).
