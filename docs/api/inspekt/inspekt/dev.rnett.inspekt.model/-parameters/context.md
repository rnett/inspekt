//[inspekt](../../../index.md)/[dev.rnett.inspekt.model](../index.md)/[Parameters](index.md)/[context](context.md)

# context

[common]\
fun [context](context.md)(indexInContextParameters: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)): [Parameter](../-parameter/index.md)

Gets the context parameter at [indexInContextParameters](context.md) in the list of context parameters.

#### Throws

| | |
|---|---|
| [IndexOutOfBoundsException](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-index-out-of-bounds-exception/index.html) | if [indexInContextParameters](context.md) is out of bounds for the context parameters. |

[common]\
val [context](context.md): [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Parameter](../-parameter/index.md)&gt;

Gets the context parameters. Does not allocate a new list.
