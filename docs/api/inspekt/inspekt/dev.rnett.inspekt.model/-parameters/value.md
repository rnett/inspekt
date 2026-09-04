//[inspekt](../../../index.md)/[dev.rnett.inspekt.model](../index.md)/[Parameters](index.md)/[value](value.md)

# value

[common]\
fun [value](value.md)(indexInValueParameters: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)): [Parameter](../-parameter/index.md)

Gets the value parameter at [indexInValueParameters](value.md) in the list of value parameters.

#### Throws

| | |
|---|---|
| [IndexOutOfBoundsException](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-index-out-of-bounds-exception/index.html) | if [indexInValueParameters](value.md) is out of bounds for the value parameters. |

[common]\
val [value](value.md): [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Parameter](../-parameter/index.md)&gt;

Gets the value parameters. Does not allocate a new list.
