//[inspekt](../../../index.md)/[dev.rnett.inspekt.model](../index.md)/[Parameters](index.md)/[globalIndex](global-index.md)

# globalIndex

[common]\
fun [globalIndex](global-index.md)(kind: [Parameter.Kind](../-parameter/-kind/index.md), indexInKind: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)

Gets the index in this list of a parameter at [indexInKind](global-index.md) in its [kind](global-index.md).

For example, to get the index in this list of the second value parameter, use `globalIndex(Kind.VALUE, 2)`.

Applies bounds and presence checks.

#### Throws

| | |
|---|---|
| [IndexOutOfBoundsException](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-index-out-of-bounds-exception/index.html) | if no parameters of [kind](global-index.md) are present, or [indexInKind](global-index.md) is out of bounds for the parameters of [kind](global-index.md). |
