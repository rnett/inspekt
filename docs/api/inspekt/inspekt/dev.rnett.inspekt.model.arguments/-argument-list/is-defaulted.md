//[inspekt](../../../index.md)/[dev.rnett.inspekt.model.arguments](../index.md)/[ArgumentList](index.md)/[isDefaulted](is-defaulted.md)

# isDefaulted

[common]\
fun [isDefaulted](is-defaulted.md)(globalIndex: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)

Returns true if the argument for the parameter at [globalIndex](is-defaulted.md) is defaulted (i.e. not set, and the parameter has a default value).

[common]\
fun [isDefaulted](is-defaulted.md)(parameter: [Parameter](../../dev.rnett.inspekt.model/-parameter/index.md)): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)

Returns true if the argument for [parameter](is-defaulted.md) is defaulted (i.e. not set, and the parameter has a default value).

[common]\
fun [isDefaulted](is-defaulted.md)(kind: [Parameter.Kind](../../dev.rnett.inspekt.model/-parameter/-kind/index.md), indexInKind: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)

Returns true if the argument for the [kind](is-defaulted.md) parameter at [indexInKind](is-defaulted.md) is defaulted (i.e. not set, and the parameter has a default value).
