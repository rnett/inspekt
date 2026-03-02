//[inspekt](../../../index.md)/[dev.rnett.inspekt.model](../index.md)/[FunctionLike](index.md)/[invokeSuspend](invoke-suspend.md)

# invokeSuspend

[common]\
inline suspend fun [invokeSuspend](invoke-suspend.md)(arguments: [ArgumentsBuilder](../../dev.rnett.inspekt.model.arguments/-arguments-builder/index.md)): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?

suspend fun [invokeSuspend](invoke-suspend.md)(arguments: [ArgumentList](../../dev.rnett.inspekt.model.arguments/-argument-list/index.md)): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?

Invoke the referenced `suspend` function with the given parameters. If the function is not suspend, this will still invoke it without an error.

#### Throws

| | |
|---|---|
| [FunctionInvocationException](../../dev.rnett.inspekt.exceptions/-function-invocation-exception/index.md) | if invocation fails |
