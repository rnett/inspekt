//[inspekt](../../../index.md)/[dev.rnett.inspekt.model](../index.md)/[Inspektion](index.md)/[set](set.md)

# set

[common]\
operator fun [set](set.md)(receiver: [T](index.md)?, name: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), value: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?)

Sets the value of the single property named [name](set.md) to [value](set.md) with [receiver](set.md).

If the property is static, [receiver](set.md) is ignored. [receiver](set.md) must be non-null for non-static properties.

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
operator fun [set](set.md)(receiver: [T](index.md)?, name: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), argumentsBuilder: [ArgumentsBuilder](../../dev.rnett.inspekt.model.arguments/-arguments-builder/index.md))

Sets the value of the single property named [name](set.md) with [receiver](set.md) and arguments [argumentsBuilder](set.md).

If the property is static, [receiver](set.md) is ignored. [receiver](set.md) must be non-null for non-static properties, or be set in [argumentsBuilder](set.md).

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
