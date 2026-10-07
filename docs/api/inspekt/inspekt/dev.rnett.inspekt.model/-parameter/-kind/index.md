//[inspekt](../../../../index.md)/[dev.rnett.inspekt.model](../../index.md)/[Parameter](../index.md)/[Kind](index.md)

# Kind

[common]\
enum [Kind](index.md) : [Enum](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-enum/index.html)&lt;[Parameter.Kind](index.md)&gt; 

The kind of a parameter.

## Entries

| | |
|---|---|
| [DISPATCH](-d-i-s-p-a-t-c-h/index.md) | [common]<br>[DISPATCH](-d-i-s-p-a-t-c-h/index.md)<br>A dispatch receiver parameter. For member functions, it is the `this` from the enclosing class. Top-level functions do not have dispatch receivers. |
| [CONTEXT](-c-o-n-t-e-x-t/index.md) | [common]<br>[CONTEXT](-c-o-n-t-e-x-t/index.md)<br>A context parameter, declared with `context(parameter: Type, ...)` |
| [EXTENSION](-e-x-t-e-n-s-i-o-n/index.md) | [common]<br>[EXTENSION](-e-x-t-e-n-s-i-o-n/index.md)<br>An extension parameter, declared with `fun Type.foo(...)`. |
| [VALUE](-v-a-l-u-e/index.md) | [common]<br>[VALUE](-v-a-l-u-e/index.md)<br>A value parameter, declared with `fun foo(parameter: Type, ...)` |

## Properties

| Name | Summary |
|---|---|
| [entries](entries.md) | [common]<br>val [entries](entries.md): [EnumEntries](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.enums/-enum-entries/index.html)&lt;[Parameter.Kind](index.md)&gt;<br>Returns a representation of an immutable list of all enum entries, in the order they're declared. |
| [humanName](human-name.md) | [common]<br>val [humanName](human-name.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |
| [name](../../-type-parameter/-variance/-o-u-t/index.md#-372974862%2FProperties%2F162778885) | [common]<br>expect val [name](../../-type-parameter/-variance/-o-u-t/index.md#-372974862%2FProperties%2F162778885): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |
| [ordinal](../../-type-parameter/-variance/-o-u-t/index.md#-739389684%2FProperties%2F162778885) | [common]<br>expect val [ordinal](../../-type-parameter/-variance/-o-u-t/index.md#-739389684%2FProperties%2F162778885): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) |

## Functions

| Name | Summary |
|---|---|
| [valueOf](value-of.md) | [common]<br>fun [valueOf](value-of.md)(value: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)): [Parameter.Kind](index.md)<br>Returns the enum constant of this type with the specified name. The string must match exactly an identifier used to declare an enum constant in this type. (Extraneous whitespace characters are not permitted.) |
| [values](values.md) | [common]<br>fun [values](values.md)(): [Array](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-array/index.html)&lt;[Parameter.Kind](index.md)&gt;<br>Returns an array containing the constants of this enum type, in the order they're declared. |
