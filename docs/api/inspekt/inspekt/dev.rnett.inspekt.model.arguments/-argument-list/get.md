//[inspekt](../../../index.md)/[dev.rnett.inspekt.model.arguments](../index.md)/[ArgumentList](index.md)/[get](get.md)

# get

[common]\
open operator override fun [get](get.md)(index: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?

Get the argument for the parameter at [index](get.md), or null if one was not set. To distinguish between explicitly passing `null` and using the default value, use [isDefaulted](is-defaulted.md).

#### See also

| |
|---|
| [ArgumentList.isDefaulted](is-defaulted.md) |

[common]\
operator fun [get](get.md)(name: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?

Get the argument for the parameter with the given [name](get.md), or null if one was not set. To distinguish between explicitly passing `null` and using the default value, use [isDefaulted](is-defaulted.md).

#### See also

| |
|---|
| [ArgumentList.isDefaulted](is-defaulted.md) |

#### Throws

| | |
|---|---|
| [IllegalArgumentException](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-illegal-argument-exception/index.html) | if [name](get.md) == `"this"`, because the parameter would be ambiguous. |

[common]\
operator fun [get](get.md)(parameter: [Parameter](../../dev.rnett.inspekt.model/-parameter/index.md)): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?

Get the argument for [parameter](get.md), or null if one was not set. To distinguish between explicitly passing `null` and using the default value, use [isDefaulted](is-defaulted.md).

#### See also

| |
|---|
| [ArgumentList.isDefaulted](is-defaulted.md) |

[common]\
operator fun [get](get.md)(kind: [Parameter.Kind](../../dev.rnett.inspekt.model/-parameter/-kind/index.md), indexInKind: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?

Get the argument for the [kind](get.md) parameter at [indexInKind](get.md), or null if one was not set. To distinguish between explicitly passing `null` and using the default value, use [isDefaulted](is-defaulted.md).

#### See also

| |
|---|
| [ArgumentList.isDefaulted](is-defaulted.md) |
