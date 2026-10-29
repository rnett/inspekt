//[inspekt](../../../../index.md)/[dev.rnett.inspekt.model.arguments](../../index.md)/[ArgumentList](../index.md)/[Builder](index.md)/[context](context.md)

# context

[common]\
fun [context](context.md)(vararg values: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?, offset: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) = 0)

Sets multiple argument values for context parameters based on [values](context.md). For each item in [values](context.md), sets the argument value for the parameter at index `[contextParameterStart + idx + offset]`.

#### Throws

| | |
|---|---|
| [IndexOutOfBoundsException](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-index-out-of-bounds-exception/index.html) | if any index is out of bounds for these parameters. |
