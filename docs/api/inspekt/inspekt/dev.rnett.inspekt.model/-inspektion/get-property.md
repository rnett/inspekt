//[inspekt](../../../index.md)/[dev.rnett.inspekt.model](../index.md)/[Inspektion](index.md)/[getProperty](get-property.md)

# getProperty

[common]\
inline fun [getProperty](get-property.md)(receiver: [T](index.md)?, name: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), crossinline argumentsBuilder: [ArgumentsBuilder](../../dev.rnett.inspekt.model.arguments/-arguments-builder/index.md) = {}): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?

Gets the value of the single property named [name](get-property.md) with [receiver](get-property.md) and [argumentsBuilder](get-property.md).

If the property is static, [receiver](get-property.md) is ignored. [receiver](get-property.md) must be non-null for non-static properties, or be set in [argumentsBuilder](get-property.md).

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
