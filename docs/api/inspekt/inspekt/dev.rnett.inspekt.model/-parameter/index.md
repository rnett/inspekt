//[inspekt](../../../index.md)/[dev.rnett.inspekt.model](../index.md)/[Parameter](index.md)

# Parameter

[common]\
data class [Parameter](index.md) : [AnnotatedElement](../-annotated-element/index.md)

A parameter of a function or other callable declaration.

## Types

| Name | Summary |
|---|---|
| [Kind](-kind/index.md) | [common]<br>enum [Kind](-kind/index.md) : [Enum](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-enum/index.html)&lt;[Parameter.Kind](-kind/index.md)&gt; <br>The kind of a parameter. |

## Properties

| Name | Summary |
|---|---|
| [annotations](annotations.md) | [common]<br>open override val [annotations](annotations.md): [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Annotation](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-annotation/index.html)&gt; |
| [globalIndex](global-index.md) | [common]<br>val [globalIndex](global-index.md): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)<br>The global index of the parameter in the list of all parameters. |
| [hasDefault](has-default.md) | [common]<br>val [hasDefault](has-default.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)<br>Whether the parameter has a default value. |
| [indexInKind](index-in-kind.md) | [common]<br>val [indexInKind](index-in-kind.md): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)<br>The index of the parameter in the list of parameters of its [Kind](-kind/index.md). |
| [kind](kind.md) | [common]<br>val [kind](kind.md): [Parameter.Kind](-kind/index.md)<br>The kind of the parameter: dispatch, context, extension, or value. |
| [name](name.md) | [common]<br>val [name](name.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)<br>The parameter's name. |
| [type](type.md) | [common]<br>val [type](type.md): [KType](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.reflect/-k-type/index.html)<br>The parameter's type. |

## Functions

| Name | Summary |
|---|---|
| [annotation](../annotation.md) | [common]<br>inline fun &lt;[T](../annotation.md)&gt; [AnnotatedElement](../-annotated-element/index.md).[annotation](../annotation.md)(): [T](../annotation.md)?<br>Get the first annotation of type [T](../annotation.md), or null if none is found. |
| [annotations](../annotations.md) | [common]<br>inline fun &lt;[T](../annotations.md)&gt; [AnnotatedElement](../-annotated-element/index.md).[annotations](../annotations.md)(): [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[T](../annotations.md)&gt;<br>Get all annotations of type [T](../annotations.md). |
| [checkValue](check-value.md) | [common]<br>fun [checkValue](check-value.md)(value: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?)<br>Throws an exception if the parameter cannot accept the given value. Not throwing does not guarantee acceptance (although it typically will be accepted). |
| [possiblyAcceptsValue](possibly-accepts-value.md) | [common]<br>fun [possiblyAcceptsValue](possibly-accepts-value.md)(value: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)<br>Returns true if the parameter can possibly accept the given value. Returning `true` does not guarantee acceptance (although it typically will be accepted), but returning `false` does guarantee rejection. |
| [toString](to-string.md) | [common]<br>open override fun [toString](to-string.md)(): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |
