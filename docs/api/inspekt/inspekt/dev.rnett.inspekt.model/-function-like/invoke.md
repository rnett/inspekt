//[inspekt](../../../index.md)/[dev.rnett.inspekt.model](../index.md)/[FunctionLike](index.md)/[invoke](invoke.md)

# invoke

[common]\
inline operator fun [invoke](invoke.md)(arguments: [ArgumentsBuilder](../../dev.rnett.inspekt.model.arguments/-arguments-builder/index.md)): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?

operator fun [invoke](invoke.md)(arguments: [ArgumentList](../../dev.rnett.inspekt.model.arguments/-argument-list/index.md)): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?

Invoke the referenced function with the given parameters. Will error if the underlying function is `suspend`.

#### Throws

| | |
|---|---|
| [FunctionInvocationException](../../dev.rnett.inspekt.exceptions/-function-invocation-exception/index.md) | if invocation fails |
