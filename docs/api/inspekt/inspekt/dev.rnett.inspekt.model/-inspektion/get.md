//[inspekt](../../../index.md)/[dev.rnett.inspekt.model](../index.md)/[Inspektion](index.md)/[get](get.md)

# get

[common]\
inline operator fun [get](get.md)(receiver: [T](index.md)?, name: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), crossinline argumentsBuilder: [ArgumentsBuilder](../../dev.rnett.inspekt.model.arguments/-arguments-builder/index.md) = {}): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?

Gets the value of the single property named [name](get.md) with [receiver](get.md) and [argumentsBuilder](get.md).

If the property is static, [receiver](get.md) is ignored. [receiver](get.md) must be non-null for non-static properties, or be set in [argumentsBuilder](get.md).

#### See also

| |
|---|
| [Inspektion.property](property.md) |
| [Property.get](../-property/get.md) |
| [FunctionLike.invoke](../-function-like/invoke.md) |

#### Throws

| | |
|---|---|
| [FunctionInvocationException](../../dev.rnett.inspekt.exceptions/-function-invocation-exception/index.md) | if the property getter cannot be invoked. |
| [NoSuchElementException](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-no-such-element-exception/index.html) | if no property with that name exists. |
| [IllegalArgumentException](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-illegal-argument-exception/index.html) | if more than one property with that name exist. |
