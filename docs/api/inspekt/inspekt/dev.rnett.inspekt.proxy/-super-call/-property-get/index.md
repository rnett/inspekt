//[inspekt](../../../../index.md)/[dev.rnett.inspekt.proxy](../../index.md)/[SuperCall](../index.md)/[PropertyGet](index.md)

# PropertyGet

[common]\
data class [PropertyGet](index.md)(val superProperty: [Property](../../../dev.rnett.inspekt.model/-property/index.md), val superFun: [PropertyGetter](../../../dev.rnett.inspekt.model/-property-getter/index.md), val args: [ArgumentList](../../../dev.rnett.inspekt.model.arguments/-argument-list/index.md)) : [SuperCall.PropertyAccess](../-property-access/index.md)

A call to a property's getter.

Note that [superGetter](super-getter.md) and `superProperty.getter` may not be the same, if the original getter was overridden by an intermediate superclass. [superGetter](super-getter.md) is the getter that the getter is overriding, while [superProperty](../../../../../inspekt/dev.rnett.inspekt.proxy/-super-call/-property-get/--root--.md) is the original property that is being overridden. **Any calls should go to** [**superGetter**](super-getter.md), which is set as [superFun](../../../../../inspekt/dev.rnett.inspekt.proxy/-super-call/-property-get/--root--.md).

## Constructors

| | |
|---|---|
| [PropertyGet](-property-get.md) | [common]<br>constructor(superProperty: [Property](../../../dev.rnett.inspekt.model/-property/index.md), superFun: [PropertyGetter](../../../dev.rnett.inspekt.model/-property-getter/index.md), args: [ArgumentList](../../../dev.rnett.inspekt.model.arguments/-argument-list/index.md)) |

## Properties

| Name | Summary |
|---|---|
| [args](args.md) | [common]<br>open override val [args](args.md): [ArgumentList](../../../dev.rnett.inspekt.model.arguments/-argument-list/index.md)<br>The arguments the function is being called with. |
| [functionName](../function-name.md) | [common]<br>val [functionName](../function-name.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)<br>The short name of the function called. May be a name like `<get-foo>` for a property accessor. |
| [isSuperAbstract](../is-super-abstract.md) | [common]<br>val [isSuperAbstract](../is-super-abstract.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)<br>Whether [superFun](../super-fun.md) is abstract. |
| [isSuperCallable](../is-super-callable.md) | [common]<br>val [isSuperCallable](../is-super-callable.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)<br>Whether [superFun](../super-fun.md) can be invoked. |
| [isSuspending](../is-suspending.md) | [common]<br>val [isSuspending](../is-suspending.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)<br>Whether [superFun](../super-fun.md) (and thus this call) is suspending. |
| [parameters](../parameters.md) | [common]<br>val [parameters](../parameters.md): [Parameters](../../../dev.rnett.inspekt.model/-parameters/index.md)<br>The parameters of the function originally being called. |
| [propertyName](../property-name.md) | [common]<br>val [propertyName](../property-name.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?<br>If this is an accessor call, return the short name of the property. |
| [superFun](super-fun.md) | [common]<br>open override val [superFun](super-fun.md): [PropertyGetter](../../../dev.rnett.inspekt.model/-property-getter/index.md)<br>The function originally being called. |
| [superGetter](super-getter.md) | [common]<br>val [superGetter](super-getter.md): [PropertyGetter](../../../dev.rnett.inspekt.model/-property-getter/index.md) |
| [superProperty](super-property.md) | [common]<br>open override val [superProperty](super-property.md): [Property](../../../dev.rnett.inspekt.model/-property/index.md) |

## Functions

| Name | Summary |
|---|---|
| [callSuper](../call-super.md) | [common]<br>fun [callSuper](../call-super.md)(): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?<br>Call [superFun](../super-fun.md) with the call's arguments.<br>[common]<br>inline fun [callSuper](../call-super.md)(builder: [ArgumentsBuilder](../../../dev.rnett.inspekt.model.arguments/-arguments-builder/index.md)): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?<br>Call [superFun](../super-fun.md) with the given arguments. |
| [callSuperSuspend](../call-super-suspend.md) | [common]<br>suspend fun [callSuperSuspend](../call-super-suspend.md)(): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?<br>Call [superFun](../super-fun.md) with the call's arguments.<br>[common]<br>inline suspend fun [callSuperSuspend](../call-super-suspend.md)(builder: [ArgumentsBuilder](../../../dev.rnett.inspekt.model.arguments/-arguments-builder/index.md)): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?<br>Call [superFun](../super-fun.md) with the given arguments. |
| [copyArgs](../copy-args.md) | [common]<br>fun [ArgumentList.Builder](../../../dev.rnett.inspekt.model.arguments/-argument-list/-builder/index.md).[copyArgs](../copy-args.md)()<br>Copy the call's arguments into this argument list. |
