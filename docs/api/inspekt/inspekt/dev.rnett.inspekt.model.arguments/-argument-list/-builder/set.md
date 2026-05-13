//[inspekt](../../../../index.md)/[dev.rnett.inspekt.model.arguments](../../index.md)/[ArgumentList](../index.md)/[Builder](index.md)/[set](set.md)

# set

[common]\
operator fun [set](set.md)(globalIndex: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html), value: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?)

Sets the argument value for the parameter at index [globalIndex](set.md).

#### Throws

| | |
|---|---|
| [IndexOutOfBoundsException](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-index-out-of-bounds-exception/index.html) | if [globalIndex](set.md) is out of bounds for these parameters. |

[common]\
operator fun [set](set.md)(kind: [Parameter.Kind](../../../dev.rnett.inspekt.model/-parameter/-kind/index.md), indexInKind: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html), value: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?)

Sets the argument value for the [kind](set.md) parameter at [indexInKind](set.md).

#### Throws

| | |
|---|---|
| [IndexOutOfBoundsException](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-index-out-of-bounds-exception/index.html) | if [indexInKind](set.md) is out of bounds for parameters of kind [kind](set.md). |

[common]\
operator fun [set](set.md)(name: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), value: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?)

Sets the value of the parameter with the given [name](set.md).

#### Throws

| | |
|---|---|
| [IllegalArgumentException](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-illegal-argument-exception/index.html) | if no parameter with the given [name](set.md) is found, or if [name](set.md) == `"this"`.. |

[common]\
operator fun [set](set.md)(parameter: [Parameter](../../../dev.rnett.inspekt.model/-parameter/index.md), value: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?)

Sets the value of the given parameter.

#### Throws

| | |
|---|---|
| [IllegalArgumentException](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-illegal-argument-exception/index.html) | if [parameters](../../../../../inspekt/dev.rnett.inspekt.model.arguments/-argument-list/-builder/--root--.md) is not in the parameter set for this builder. |

[common]\
fun [set](set.md)(vararg values: [Pair](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-pair/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?&gt;)

Set multiple arguments based on the parameter names.

#### Throws

| | |
|---|---|
| [IllegalArgumentException](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-illegal-argument-exception/index.html) | if no parameter with the given name is found, or if name == `"this"`.. |
