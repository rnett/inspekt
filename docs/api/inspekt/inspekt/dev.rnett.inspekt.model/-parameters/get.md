//[inspekt](../../../index.md)/[dev.rnett.inspekt.model](../index.md)/[Parameters](index.md)/[get](get.md)

# get

[common]\
operator fun [get](get.md)(name: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)): [Parameter](../-parameter/index.md)?

Get the parameter named [name](get.md), if present. Cannot be used with `"this"`, as it could refer to the dispatch or extension receiver.

#### Throws

| | |
|---|---|
| [IllegalArgumentException](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-illegal-argument-exception/index.html) | if [name](get.md) is `"this"` |

[common]\
operator fun [get](get.md)(kind: [Parameter.Kind](../-parameter/-kind/index.md), indexInKind: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)): [Parameter](../-parameter/index.md)

Get a parameter by its index in its own kind. For example, `get(Kind.VALUE, 2)` gets the third value parameter.

#### See also

| |
|---|
| [Parameters.value](value.md) |
| [Parameters.context](context.md) |
| [Parameters.dispatch](dispatch.md) |
| [Parameters.extension](extension.md) |
