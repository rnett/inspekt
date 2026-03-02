//[inspekt](../../../index.md)/[dev.rnett.inspekt.model](../index.md)/[Inspektion](index.md)/[setProperty](set-property.md)

# setProperty

[common]\
inline fun [setProperty](set-property.md)(receiver: [T](index.md)?, name: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), crossinline argumentsBuilder: [ArgumentsBuilder](../../dev.rnett.inspekt.model.arguments/-arguments-builder/index.md) = {})

Sets the value of the single property named [name](set-property.md) with [receiver](set-property.md) and [argumentsBuilder](set-property.md).

If the property is static, [receiver](set-property.md) is ignored. [receiver](set-property.md) must be non-null for non-static properties, or be set in [argumentsBuilder](set-property.md).

#### See also

| |
|---|
| [Inspektion.property](property.md) |
| [MutableProperty.set](../-mutable-property/set.md) |
| [FunctionLike.invoke](../-function-like/invoke.md) |

#### Throws

| | |
|---|---|
| [FunctionInvocationException](../../dev.rnett.inspekt.exceptions/-function-invocation-exception/index.md) | if the property setter cannot be invoked or the property is `val`. |
| [NoSuchElementException](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-no-such-element-exception/index.html) | if no property with that name exists. |
| [IllegalArgumentException](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-illegal-argument-exception/index.html) | if more than one property with that name exist. |

[common]\
inline fun [setProperty](set-property.md)(receiver: [T](index.md)?, name: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), value: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?, crossinline argumentsBuilder: [ArgumentsBuilder](../../dev.rnett.inspekt.model.arguments/-arguments-builder/index.md) = {})

Sets the value of the single property named [name](set-property.md) to [value](set-property.md) with [receiver](set-property.md) and [argumentsBuilder](set-property.md) additional arguments.

If the property is static, [receiver](set-property.md) is ignored. [receiver](set-property.md) must be non-null for non-static properties, or be set in [argumentsBuilder](set-property.md).

#### See also

| |
|---|
| [Inspektion.property](property.md) |
| [MutableProperty.set](../-mutable-property/set.md) |
| [FunctionLike.invoke](../-function-like/invoke.md) |

#### Throws

| | |
|---|---|
| [FunctionInvocationException](../../dev.rnett.inspekt.exceptions/-function-invocation-exception/index.md) | if the property setter cannot be invoked or the property is `val`. |
| [NoSuchElementException](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-no-such-element-exception/index.html) | if no property with that name exists. |
| [IllegalArgumentException](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-illegal-argument-exception/index.html) | if more than one property with that name exist. |
