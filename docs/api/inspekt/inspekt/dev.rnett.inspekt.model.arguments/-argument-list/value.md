//[inspekt](../../../index.md)/[dev.rnett.inspekt.model.arguments](../index.md)/[ArgumentList](index.md)/[value](value.md)

# value

[common]\
fun [value](value.md)(index: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?

Gets the value parameter at [index](value.md), if it is set. To distinguish between explicitly passing `null` and using the default value, use [isDefaulted](is-defaulted.md).

#### See also

| |
|---|
| [ArgumentList.isDefaulted](is-defaulted.md) |

#### Throws

| | |
|---|---|
| [IndexOutOfBoundsException](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-index-out-of-bounds-exception/index.html) | if [index](value.md) is out of bounds for the value parameters. |
