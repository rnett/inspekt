//[inspekt](../../../../index.md)/[dev.rnett.inspekt.model.arguments](../../index.md)/[ArgumentList](../index.md)/[Builder](index.md)/[value](value.md)

# value

[common]\
fun [value](value.md)(vararg values: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?, offset: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) = 0)

Sets multiple argument values for value parameters based on [values](value.md). For each item in [values](value.md), sets the argument value for the parameter at index `[valueParameterStart + idx + offset]`.

#### Throws

| | |
|---|---|
| [IndexOutOfBoundsException](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-index-out-of-bounds-exception/index.html) | if any index is out of bounds for these parameters. |
