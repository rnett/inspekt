//[inspekt](../../index.md)/[dev.rnett.inspekt.model.arguments](index.md)/[buildArguments](build-arguments.md)

# buildArguments

[common]\
inline fun [Callable](../dev.rnett.inspekt.model/-callable/index.md).[buildArguments](build-arguments.md)(builder: [ArgumentsBuilder](-arguments-builder/index.md)): [ArgumentList](-argument-list/index.md)

Builds an [ArgumentList](-argument-list/index.md) for a set of parameters. Parameters which are not set will use their default values if they have one. Successfully building an [ArgumentList](-argument-list/index.md) guarantees that values are provided for all parameters without default values.
