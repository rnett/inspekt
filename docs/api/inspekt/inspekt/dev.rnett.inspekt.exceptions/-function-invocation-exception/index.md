//[inspekt](../../../index.md)/[dev.rnett.inspekt.exceptions](../index.md)/[FunctionInvocationException](index.md)

# FunctionInvocationException

[common]\
class [FunctionInvocationException](index.md)(val name: [QualifiedName](../../dev.rnett.inspekt.model.name/-qualified-name/index.md), val cause: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html)) : [RuntimeException](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-runtime-exception/index.html)

Exception thrown when a function invocation fails.

## Constructors

| | |
|---|---|
| [FunctionInvocationException](-function-invocation-exception.md) | [common]<br>constructor(name: [QualifiedName](../../dev.rnett.inspekt.model.name/-qualified-name/index.md), cause: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [cause](cause.md) | [common]<br>open override val [cause](cause.md): [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html) |
| [message](../-inspekt-not-intrinsified-exception/index.md#1824300659%2FProperties%2F162778885) | [common]<br>expect open val [message](../-inspekt-not-intrinsified-exception/index.md#1824300659%2FProperties%2F162778885): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)? |
| [name](name.md) | [common]<br>val [name](name.md): [QualifiedName](../../dev.rnett.inspekt.model.name/-qualified-name/index.md) |
