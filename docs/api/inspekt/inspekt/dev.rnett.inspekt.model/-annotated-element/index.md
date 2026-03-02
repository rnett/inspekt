//[inspekt](../../../index.md)/[dev.rnett.inspekt.model](../index.md)/[AnnotatedElement](index.md)

# AnnotatedElement

interface [AnnotatedElement](index.md)

An element which has annotations.

#### Inheritors

| |
|---|
| [Callable](../-callable/index.md) |
| [Inspektion](../-inspektion/index.md) |
| [Parameter](../-parameter/index.md) |
| [TypeParameter](../-type-parameter/index.md) |

## Properties

| Name | Summary |
|---|---|
| [annotations](annotations.md) | [common]<br>abstract val [annotations](annotations.md): [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Annotation](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-annotation/index.html)&gt; |

## Functions

| Name | Summary |
|---|---|
| [annotation](../annotation.md) | [common]<br>inline fun &lt;[T](../annotation.md)&gt; [AnnotatedElement](index.md).[annotation](../annotation.md)(): [T](../annotation.md)?<br>Get the first annotation of type [T](../annotation.md), or null if none is found. |
| [annotations](../annotations.md) | [common]<br>inline fun &lt;[T](../annotations.md)&gt; [AnnotatedElement](index.md).[annotations](../annotations.md)(): [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[T](../annotations.md)&gt;<br>Get all annotations of type [T](../annotations.md). |
