//[inspekt](../../../index.md)/[dev.rnett.inspekt.proxy](../index.md)/[SuperCall](index.md)

# SuperCall

sealed class [SuperCall](index.md)

The function call being handled by the proxy. Includes both the original function ([superFun](super-fun.md)) and the call's [args](args.md).

#### Inheritors

| |
|---|
| [PropertyAccess](-property-access/index.md) |
| [FunctionCall](-function-call/index.md) |

## Types

| Name | Summary |
|---|---|
| [FunctionCall](-function-call/index.md) | [common]<br>data class [FunctionCall](-function-call/index.md)(val superFunction: [SimpleFunction](../../dev.rnett.inspekt.model/-simple-function/index.md), val args: [ArgumentList](../../dev.rnett.inspekt.model.arguments/-argument-list/index.md)) : [SuperCall](index.md)<br>A call to a simple function. |
| [PropertyAccess](-property-access/index.md) | [common]<br>sealed class [PropertyAccess](-property-access/index.md) : [SuperCall](index.md) |
| [PropertyGet](-property-get/index.md) | [common]<br>data class [PropertyGet](-property-get/index.md)(val superProperty: [Property](../../dev.rnett.inspekt.model/-property/index.md), val superFun: [PropertyGetter](../../dev.rnett.inspekt.model/-property-getter/index.md), val args: [ArgumentList](../../dev.rnett.inspekt.model.arguments/-argument-list/index.md)) : [SuperCall.PropertyAccess](-property-access/index.md)<br>A call to a property's getter. |
| [PropertySet](-property-set/index.md) | [common]<br>data class [PropertySet](-property-set/index.md)(val superProperty: [Property](../../dev.rnett.inspekt.model/-property/index.md), val superFun: [PropertySetter](../../dev.rnett.inspekt.model/-property-setter/index.md), val args: [ArgumentList](../../dev.rnett.inspekt.model.arguments/-argument-list/index.md)) : [SuperCall.PropertyAccess](-property-access/index.md)<br>A call to a property's setter. |

## Properties

| Name | Summary |
|---|---|
| [args](args.md) | [common]<br>abstract val [args](args.md): [ArgumentList](../../dev.rnett.inspekt.model.arguments/-argument-list/index.md)<br>The arguments the function is being called with. |
| [functionName](function-name.md) | [common]<br>val [functionName](function-name.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)<br>The short name of the function called. May be a name like `<get-foo>` for a property accessor. |
| [isSuperAbstract](is-super-abstract.md) | [common]<br>val [isSuperAbstract](is-super-abstract.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)<br>Whether [superFun](super-fun.md) is abstract. |
| [isSuperCallable](is-super-callable.md) | [common]<br>val [isSuperCallable](is-super-callable.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)<br>Whether [superFun](super-fun.md) can be invoked. |
| [isSuspending](is-suspending.md) | [common]<br>val [isSuspending](is-suspending.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)<br>Whether [superFun](super-fun.md) (and thus this call) is suspending. |
| [parameters](parameters.md) | [common]<br>val [parameters](parameters.md): [Parameters](../../dev.rnett.inspekt.model/-parameters/index.md)<br>The parameters of the function originally being called. |
| [propertyName](property-name.md) | [common]<br>val [propertyName](property-name.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?<br>If this is an accessor call, return the short name of the property. |
| [superFun](super-fun.md) | [common]<br>abstract val [superFun](super-fun.md): [Function](../../dev.rnett.inspekt.model/-function/index.md)<br>The function originally being called. |
| [superProperty](super-property.md) | [common]<br>open val [superProperty](super-property.md): [Property](../../dev.rnett.inspekt.model/-property/index.md)? |

## Functions

| Name | Summary |
|---|---|
| [callSuper](call-super.md) | [common]<br>fun [callSuper](call-super.md)(): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?<br>Call [superFun](super-fun.md) with the call's arguments.<br>[common]<br>inline fun [callSuper](call-super.md)(builder: [ArgumentsBuilder](../../dev.rnett.inspekt.model.arguments/-arguments-builder/index.md)): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?<br>Call [superFun](super-fun.md) with the given arguments. |
| [callSuperSuspend](call-super-suspend.md) | [common]<br>suspend fun [callSuperSuspend](call-super-suspend.md)(): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?<br>Call [superFun](super-fun.md) with the call's arguments.<br>[common]<br>inline suspend fun [callSuperSuspend](call-super-suspend.md)(builder: [ArgumentsBuilder](../../dev.rnett.inspekt.model.arguments/-arguments-builder/index.md)): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?<br>Call [superFun](super-fun.md) with the given arguments. |
| [copyArgs](copy-args.md) | [common]<br>fun [ArgumentList.Builder](../../dev.rnett.inspekt.model.arguments/-argument-list/-builder/index.md).[copyArgs](copy-args.md)()<br>Copy the call's arguments into this argument list. |
