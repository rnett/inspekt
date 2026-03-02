//[inspekt](../../../../index.md)/[dev.rnett.inspekt.proxy](../../index.md)/[SuperCall](../index.md)/[PropertySet](index.md)

# PropertySet

[common]\
data class [PropertySet](index.md)(val superProperty: [Property](../../../dev.rnett.inspekt.model/-property/index.md), val superFun: [PropertySetter](../../../dev.rnett.inspekt.model/-property-setter/index.md), val args: [ArgumentList](../../../dev.rnett.inspekt.model.arguments/-argument-list/index.md)) : [SuperCall.PropertyAccess](../-property-access/index.md)

A call to a property's setter.

Note that [superSetter](super-setter.md) and `superProperty.setter` may not be the same, if the original setter was defined or overridden by an intermediate superclass - [superProperty](../../../../../inspekt/dev.rnett.inspekt.proxy/-super-call/-property-set/--root--.md) may not even have a setter. [superSetter](super-setter.md) is the setter that the setter is overriding, while [superProperty](../../../../../inspekt/dev.rnett.inspekt.proxy/-super-call/-property-set/--root--.md) is the original property that is being overridden. **Any calls should go to** [**superSetter**](super-setter.md), which is set as [superFun](../../../../../inspekt/dev.rnett.inspekt.proxy/-super-call/-property-set/--root--.md).

## Constructors

| | |
|---|---|
| [PropertySet](-property-set.md) | [common]<br>constructor(superProperty: [Property](../../../dev.rnett.inspekt.model/-property/index.md), superFun: [PropertySetter](../../../dev.rnett.inspekt.model/-property-setter/index.md), args: [ArgumentList](../../../dev.rnett.inspekt.model.arguments/-argument-list/index.md)) |

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
| [superFun](super-fun.md) | [common]<br>open override val [superFun](super-fun.md): [PropertySetter](../../../dev.rnett.inspekt.model/-property-setter/index.md)<br>The function originally being called. |
| [superProperty](super-property.md) | [common]<br>open override val [superProperty](super-property.md): [Property](../../../dev.rnett.inspekt.model/-property/index.md) |
| [superSetter](super-setter.md) | [common]<br>val [superSetter](super-setter.md): [PropertySetter](../../../dev.rnett.inspekt.model/-property-setter/index.md) |

## Functions

| Name | Summary |
|---|---|
| [callSuper](../call-super.md) | [common]<br>fun [callSuper](../call-super.md)(): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?<br>Call [superFun](../super-fun.md) with the call's arguments.<br>[common]<br>inline fun [callSuper](../call-super.md)(builder: [ArgumentsBuilder](../../../dev.rnett.inspekt.model.arguments/-arguments-builder/index.md)): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?<br>Call [superFun](../super-fun.md) with the given arguments. |
| [callSuperSuspend](../call-super-suspend.md) | [common]<br>suspend fun [callSuperSuspend](../call-super-suspend.md)(): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?<br>Call [superFun](../super-fun.md) with the call's arguments.<br>[common]<br>inline suspend fun [callSuperSuspend](../call-super-suspend.md)(builder: [ArgumentsBuilder](../../../dev.rnett.inspekt.model.arguments/-arguments-builder/index.md)): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?<br>Call [superFun](../super-fun.md) with the given arguments. |
| [copyArgs](../copy-args.md) | [common]<br>fun [ArgumentList.Builder](../../../dev.rnett.inspekt.model.arguments/-argument-list/-builder/index.md).[copyArgs](../copy-args.md)()<br>Copy the call's arguments into this argument list. |
