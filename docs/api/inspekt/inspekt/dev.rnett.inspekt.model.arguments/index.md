//[inspekt](../../index.md)/[dev.rnett.inspekt.model.arguments](index.md)

# Package-level declarations

[common]\
Builders and accessors for argument lists.

## Types

| Name | Summary |
|---|---|
| [ArgumentList](-argument-list/index.md) | [common]<br>data class [ArgumentList](-argument-list/index.md) : ArgumentsProviderV1, [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?&gt; <br>A list of argument value assignments for a set of parameters. Guarantees that an argument is provided for all parameters without default values. |
| [ArgumentsBuilder](-arguments-builder/index.md) | [common]<br>typealias [ArgumentsBuilder](-arguments-builder/index.md) = [ArgumentList.Builder](-argument-list/-builder/index.md).([Parameters](../dev.rnett.inspekt.model/-parameters/index.md)) -&gt; [Unit](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-unit/index.html)<br>A builder lambda for a [ArgumentList.Builder](-argument-list/-builder/index.md). |

## Functions

| Name | Summary |
|---|---|
| [ArgumentList](-argument-list.md) | [common]<br>inline fun [ArgumentList](-argument-list.md)(parameters: [Parameters](../dev.rnett.inspekt.model/-parameters/index.md), builder: [ArgumentsBuilder](-arguments-builder/index.md)): [ArgumentList](-argument-list/index.md)<br>Builds an [ArgumentList](-argument-list.md) for a set of parameters. Parameters which are not set will use their default values if they have one. Successfully building an [ArgumentList](-argument-list.md) guarantees that values are provided for all parameters without default values.<br>[common]<br>fun [ArgumentList](-argument-list.md)(parameters: [Parameters](../dev.rnett.inspekt.model/-parameters/index.md), args: [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?&gt;): [ArgumentList](-argument-list/index.md)<br>Builds an [ArgumentList](-argument-list.md) for a set of parameters, where all argument values are known. The size of [args](-argument-list.md) must exactly match the size of [parameters](-argument-list.md). |
| [buildArguments](build-arguments.md) | [common]<br>inline fun [Callable](../dev.rnett.inspekt.model/-callable/index.md).[buildArguments](build-arguments.md)(builder: [ArgumentsBuilder](-arguments-builder/index.md)): [ArgumentList](-argument-list/index.md)<br>Builds an [ArgumentList](-argument-list/index.md) for a set of parameters. Parameters which are not set will use their default values if they have one. Successfully building an [ArgumentList](-argument-list/index.md) guarantees that values are provided for all parameters without default values. |
