//[inspekt](../../index.md)/[dev.rnett.inspekt.model.arguments](index.md)/[ArgumentList](-argument-list.md)

# ArgumentList

[common]\
inline fun [ArgumentList](-argument-list.md)(parameters: [Parameters](../dev.rnett.inspekt.model/-parameters/index.md), builder: [ArgumentsBuilder](-arguments-builder/index.md)): [ArgumentList](-argument-list/index.md)

Builds an [ArgumentList](-argument-list.md) for a set of parameters. Parameters which are not set will use their default values if they have one. Successfully building an [ArgumentList](-argument-list.md) guarantees that values are provided for all parameters without default values.

[common]\
fun [ArgumentList](-argument-list.md)(parameters: [Parameters](../dev.rnett.inspekt.model/-parameters/index.md), args: [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?&gt;): [ArgumentList](-argument-list/index.md)

Builds an [ArgumentList](-argument-list.md) for a set of parameters, where all argument values are known. The size of [args](-argument-list.md) must exactly match the size of [parameters](-argument-list.md).
