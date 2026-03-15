//[inspekt](../../../index.md)/[dev.rnett.inspekt.model](../index.md)/[Inspektion](index.md)/[callFunction](call-function.md)

# callFunction

[common]\
inline fun [callFunction](call-function.md)(receiver: [T](index.md)?, name: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), argumentsBuilder: [ArgumentsBuilder](../../dev.rnett.inspekt.model.arguments/-arguments-builder/index.md) = {}): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?

Call the single function named [name](call-function.md) with [receiver](call-function.md) and [argumentsBuilder](call-function.md).

If the function is static, [receiver](call-function.md) is ignored. [receiver](call-function.md) must be non-null for non-static functions.

#### See also

| |
|---|
| [Inspektion.function](function.md) |
| [FunctionLike.invoke](../-function-like/invoke.md) |

#### Throws

| | |
|---|---|
| [FunctionInvocationException](../../dev.rnett.inspekt.exceptions/-function-invocation-exception/index.md) | if the function cannot be invoked or the function is suspending. |
| [NoSuchElementException](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-no-such-element-exception/index.html) | if no function with that name exists. |
| [IllegalArgumentException](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-illegal-argument-exception/index.html) | if more than one function with that name exist. |
