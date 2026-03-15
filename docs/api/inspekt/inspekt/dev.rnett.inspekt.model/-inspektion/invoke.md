//[inspekt](../../../index.md)/[dev.rnett.inspekt.model](../index.md)/[Inspektion](index.md)/[invoke](invoke.md)

# invoke

[common]\
inline operator fun [invoke](invoke.md)(receiver: [T](index.md)?, name: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), argumentsBuilder: [ArgumentsBuilder](../../dev.rnett.inspekt.model.arguments/-arguments-builder/index.md) = {}): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?

Call the single function named [name](invoke.md) with [receiver](invoke.md) and [argumentsBuilder](invoke.md).

If the function is static, [receiver](invoke.md) is ignored. [receiver](invoke.md) must be non-null for non-static functions, or be set in [argumentsBuilder](invoke.md).

#### See also

| |
|---|
| [Inspektion.function](function.md) |
| [FunctionLike.invoke](../-function-like/invoke.md) |

#### Throws

| | |
|---|---|
| [FunctionInvocationException](../../dev.rnett.inspekt.exceptions/-function-invocation-exception/index.md) | if the function cannot be invoked or is suspending. |
| [NoSuchElementException](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-no-such-element-exception/index.html) | if no function with that name exists. |
| [IllegalArgumentException](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-illegal-argument-exception/index.html) | if more than one function with that name exist. |

[common]\
inline operator fun [invoke](invoke.md)(argumentsBuilder: [ArgumentsBuilder](../../dev.rnett.inspekt.model.arguments/-arguments-builder/index.md) = {}): [T](index.md)

Calls the primary constructor of this class with [argumentsBuilder](invoke.md).

#### See also

| |
|---|
| [FunctionLike.invoke](../-function-like/invoke.md) |
| [Inspektion.primaryConstructor](primary-constructor.md) |

#### Throws

| | |
|---|---|
| [FunctionInvocationException](../../dev.rnett.inspekt.exceptions/-function-invocation-exception/index.md) | if the class has no primary constructor or it cannot be invoked. |
