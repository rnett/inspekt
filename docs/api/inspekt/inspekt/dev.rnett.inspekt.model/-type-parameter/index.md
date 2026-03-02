//[inspekt](../../../index.md)/[dev.rnett.inspekt.model](../index.md)/[TypeParameter](index.md)

# TypeParameter

[common]\
data class [TypeParameter](index.md)(val name: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), val index: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html), val variance: [TypeParameter.Variance](-variance/index.md), val erasedUpperBounds: [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[KType](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.reflect/-k-type/index.html)&gt;, val isReified: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html), val annotations: [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Annotation](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-annotation/index.html)&gt;) : [AnnotatedElement](../-annotated-element/index.md)

A type parameter.

## Constructors

| | |
|---|---|
| [TypeParameter](-type-parameter.md) | [common]<br>constructor(name: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), index: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html), variance: [TypeParameter.Variance](-variance/index.md), erasedUpperBounds: [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[KType](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.reflect/-k-type/index.html)&gt;, isReified: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html), annotations: [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Annotation](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-annotation/index.html)&gt;) |

## Types

| Name | Summary |
|---|---|
| [Variance](-variance/index.md) | [common]<br>enum [Variance](-variance/index.md) : [Enum](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-enum/index.html)&lt;[TypeParameter.Variance](-variance/index.md)&gt; |

## Properties

| Name | Summary |
|---|---|
| [annotations](annotations.md) | [common]<br>open override val [annotations](annotations.md): [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Annotation](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-annotation/index.html)&gt; |
| [erasedUpperBounds](erased-upper-bounds.md) | [common]<br>val [erasedUpperBounds](erased-upper-bounds.md): [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[KType](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.reflect/-k-type/index.html)&gt; |
| [index](--index--.md) | [common]<br>val [index](--index--.md): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) |
| [isReified](is-reified.md) | [common]<br>val [isReified](is-reified.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) |
| [kotlin](kotlin.md) | [common]<br>val [kotlin](kotlin.md): [KTypeParameter](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.reflect/-k-type-parameter/index.html) |
| [name](name.md) | [common]<br>val [name](name.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |
| [variance](variance.md) | [common]<br>val [variance](variance.md): [TypeParameter.Variance](-variance/index.md) |

## Functions

| Name | Summary |
|---|---|
| [annotation](../annotation.md) | [common]<br>inline fun &lt;[T](../annotation.md)&gt; [AnnotatedElement](../-annotated-element/index.md).[annotation](../annotation.md)(): [T](../annotation.md)?<br>Get the first annotation of type [T](../annotation.md), or null if none is found. |
| [annotations](../annotations.md) | [common]<br>inline fun &lt;[T](../annotations.md)&gt; [AnnotatedElement](../-annotated-element/index.md).[annotations](../annotations.md)(): [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[T](../annotations.md)&gt;<br>Get all annotations of type [T](../annotations.md). |
