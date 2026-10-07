//[inspekt](../../../../index.md)/[dev.rnett.inspekt.model.arguments](../../index.md)/[ArgumentList](../index.md)/[Builder](index.md)/[setAll](set-all.md)

# setAll

[common]\
fun [setAll](set-all.md)(args: [Iterable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-iterable/index.html)&lt;[Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?&gt;, offset: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) = 0)

@[JvmName](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.jvm/-jvm-name/index.html)(name = &quot;setAllArray&quot;)

fun [setAll](set-all.md)(args: [Array](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-array/index.html)&lt;[Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?&gt;, offset: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) = 0)

@[JvmName](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.jvm/-jvm-name/index.html)(name = &quot;setAllVararg&quot;)

fun [setAll](set-all.md)(vararg args: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?, offset: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) = 0)

Sets multiple argument values based on [args](set-all.md). For each item in [args](set-all.md), sets the argument value for the parameter at index `[idx + offset]`.

#### Throws

| | |
|---|---|
| [IndexOutOfBoundsException](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-index-out-of-bounds-exception/index.html) | if any index is out of bounds for these parameters. |

[common]\
fun [setAll](set-all.md)(kind: [Parameter.Kind](../../../dev.rnett.inspekt.model/-parameter/-kind/index.md), args: [Iterable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-iterable/index.html)&lt;[Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?&gt;, offset: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) = 0)

@[JvmName](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.jvm/-jvm-name/index.html)(name = &quot;setAllArray&quot;)

fun [setAll](set-all.md)(kind: [Parameter.Kind](../../../dev.rnett.inspekt.model/-parameter/-kind/index.md), args: [Array](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-array/index.html)&lt;[Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?&gt;, offset: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) = 0)

@[JvmName](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.jvm/-jvm-name/index.html)(name = &quot;setAllVararg&quot;)

fun [setAll](set-all.md)(kind: [Parameter.Kind](../../../dev.rnett.inspekt.model/-parameter/-kind/index.md), vararg args: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?, offset: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) = 0)

Sets multiple argument values for [kind](set-all.md) parameters based on [args](set-all.md). For each item in [args](set-all.md), sets the argument value for the parameter at index `[kindOffset(kind) + idx + offset]`.

#### Throws

| | |
|---|---|
| [IndexOutOfBoundsException](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-index-out-of-bounds-exception/index.html) | if any index is out of bounds for these parameters. |
