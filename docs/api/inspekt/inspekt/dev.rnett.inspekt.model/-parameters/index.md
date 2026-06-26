//[inspekt](../../../index.md)/[dev.rnett.inspekt.model](../index.md)/[Parameters](index.md)

# Parameters

[common]\
data class [Parameters](index.md) : [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Parameter](../-parameter/index.md)&gt; 

The parameters of a callable. Parameters are ordered by kind ([Parameter.Kind](../-parameter/-kind/index.md)):

- 
   Dispatch receiver (if present)
- 
   Any context parameters
- 
   Extension receiver (if present)
- 
   Any value parameters.

## Properties

| Name | Summary |
|---|---|
| [context](context.md) | [common]<br>val [context](context.md): [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Parameter](../-parameter/index.md)&gt;<br>Gets the context parameters. Does not allocate a new list. |
| [dispatch](dispatch.md) | [common]<br>val [dispatch](dispatch.md): [Parameter](../-parameter/index.md)?<br>Gets the dispatch receiver parameter, if present. |
| [extension](extension.md) | [common]<br>val [extension](extension.md): [Parameter](../-parameter/index.md)?<br>Gets the extension receiver parameter, if present. |
| [hasDispatch](has-dispatch.md) | [common]<br>val [hasDispatch](has-dispatch.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) |
| [hasExtension](has-extension.md) | [common]<br>val [hasExtension](has-extension.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) |
| [size](../../dev.rnett.inspekt.model.arguments/-argument-list/index.md#844915858%2FProperties%2F162778885) | [common]<br>open override val [size](../../dev.rnett.inspekt.model.arguments/-argument-list/index.md#844915858%2FProperties%2F162778885): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) |
| [value](value.md) | [common]<br>val [value](value.md): [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Parameter](../-parameter/index.md)&gt;<br>Gets the value parameters. Does not allocate a new list. |

## Functions

| Name | Summary |
|---|---|
| [arguments](arguments.md) | [common]<br>fun [arguments](arguments.md)(args: [Array](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-array/index.html)&lt;[Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?&gt;): [ArgumentList](../../dev.rnett.inspekt.model.arguments/-argument-list/index.md)<br>fun [arguments](arguments.md)(args: [Iterable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-iterable/index.html)&lt;[Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?&gt;): [ArgumentList](../../dev.rnett.inspekt.model.arguments/-argument-list/index.md)<br>Requires specifying a value for all parameters, aka `args.size == this.size`. |
| [buildArguments](build-arguments.md) | [common]<br>inline fun [buildArguments](build-arguments.md)(builder: [ArgumentsBuilder](../../dev.rnett.inspekt.model.arguments/-arguments-builder/index.md)): [ArgumentList](../../dev.rnett.inspekt.model.arguments/-argument-list/index.md) |
| [contains](../../dev.rnett.inspekt.model.arguments/-argument-list/index.md#765883978%2FFunctions%2F162778885) | [common]<br>open operator override fun [contains](../../dev.rnett.inspekt.model.arguments/-argument-list/index.md#765883978%2FFunctions%2F162778885)(element: [Parameter](../-parameter/index.md)): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) |
| [containsAll](../../dev.rnett.inspekt.model.arguments/-argument-list/index.md#-225903147%2FFunctions%2F162778885) | [common]<br>open override fun [containsAll](../../dev.rnett.inspekt.model.arguments/-argument-list/index.md#-225903147%2FFunctions%2F162778885)(elements: [Collection](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-collection/index.html)&lt;[Parameter](../-parameter/index.md)&gt;): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) |
| [context](context.md) | [common]<br>fun [context](context.md)(indexInContextParameters: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)): [Parameter](../-parameter/index.md)<br>Gets the context parameter at [indexInContextParameters](context.md) in the list of context parameters. |
| [count](count.md) | [common]<br>fun [count](count.md)(kind: [Parameter.Kind](../-parameter/-kind/index.md)): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)<br>Gets the number of parameters of a given kind. |
| [get](get.md) | [common]<br>operator fun [get](get.md)(name: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)): [Parameter](../-parameter/index.md)?<br>Get the parameter named [name](get.md), if present. Cannot be used with `"this"`, as it could refer to the dispatch or extension receiver.<br>[common]<br>operator fun [get](get.md)(kind: [Parameter.Kind](../-parameter/-kind/index.md), indexInKind: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)): [Parameter](../-parameter/index.md)<br>Get a parameter by its index in its own kind. For example, `get(Kind.VALUE, 2)` gets the third value parameter.<br>[common]<br>open operator override fun [get](index.md#961975567%2FFunctions%2F162778885)(index: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)): [Parameter](../-parameter/index.md) |
| [globalIndex](global-index.md) | [common]<br>fun [globalIndex](global-index.md)(kind: [Parameter.Kind](../-parameter/-kind/index.md), indexInKind: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)<br>Gets the index in this list of a parameter at [indexInKind](global-index.md) in its [kind](global-index.md). |
| [indexOf](../../dev.rnett.inspekt.model.arguments/-argument-list/index.md#-407930336%2FFunctions%2F162778885) | [common]<br>open override fun [indexOf](../../dev.rnett.inspekt.model.arguments/-argument-list/index.md#-407930336%2FFunctions%2F162778885)(element: [Parameter](../-parameter/index.md)): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) |
| [isEmpty](../../dev.rnett.inspekt.model.arguments/-argument-list/index.md#-1000881820%2FFunctions%2F162778885) | [common]<br>open override fun [isEmpty](../../dev.rnett.inspekt.model.arguments/-argument-list/index.md#-1000881820%2FFunctions%2F162778885)(): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) |
| [iterator](../../dev.rnett.inspekt.model.arguments/-argument-list/index.md#-1577986619%2FFunctions%2F162778885) | [common]<br>open operator override fun [iterator](../../dev.rnett.inspekt.model.arguments/-argument-list/index.md#-1577986619%2FFunctions%2F162778885)(): [Iterator](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-iterator/index.html)&lt;[Parameter](../-parameter/index.md)&gt; |
| [lastIndexOf](../../dev.rnett.inspekt.model.arguments/-argument-list/index.md#1327716778%2FFunctions%2F162778885) | [common]<br>open override fun [lastIndexOf](../../dev.rnett.inspekt.model.arguments/-argument-list/index.md#1327716778%2FFunctions%2F162778885)(element: [Parameter](../-parameter/index.md)): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) |
| [listIterator](../../dev.rnett.inspekt.model.arguments/-argument-list/index.md#-236165689%2FFunctions%2F162778885) | [common]<br>open override fun [listIterator](../../dev.rnett.inspekt.model.arguments/-argument-list/index.md#-236165689%2FFunctions%2F162778885)(): [ListIterator](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list-iterator/index.html)&lt;[Parameter](../-parameter/index.md)&gt;<br>open override fun [listIterator](../../dev.rnett.inspekt.model.arguments/-argument-list/index.md#845091493%2FFunctions%2F162778885)(index: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)): [ListIterator](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list-iterator/index.html)&lt;[Parameter](../-parameter/index.md)&gt; |
| [startOffset](start-offset.md) | [common]<br>fun [startOffset](start-offset.md)(kind: [Parameter.Kind](../-parameter/-kind/index.md)): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)<br>Get the start offset in the parameter list of the given [kind](start-offset.md). Returning a value does not guarantee that parameters of type [kind](start-offset.md) are present. |
| [subList](../../dev.rnett.inspekt.model.arguments/-argument-list/index.md#423386006%2FFunctions%2F162778885) | [common]<br>open override fun [subList](../../dev.rnett.inspekt.model.arguments/-argument-list/index.md#423386006%2FFunctions%2F162778885)(fromIndex: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html), toIndex: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)): [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Parameter](../-parameter/index.md)&gt; |
| [toString](to-string.md) | [common]<br>open override fun [toString](to-string.md)(): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |
| [value](value.md) | [common]<br>fun [value](value.md)(indexInValueParameters: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)): [Parameter](../-parameter/index.md)<br>Gets the value parameter at [indexInValueParameters](value.md) in the list of value parameters. |
