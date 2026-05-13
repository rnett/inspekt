//[inspekt](../../../index.md)/[dev.rnett.inspekt.model](../index.md)/[AnnotationInfo](index.md)

# AnnotationInfo

[common]\
data class [AnnotationInfo](index.md)(val annotation: [Annotation](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-annotation/index.html), val source: [KClass](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.reflect/-k-class/index.html)&lt;*&gt;?)

An annotation with its source class.

## Constructors

| | |
|---|---|
| [AnnotationInfo](-annotation-info.md) | [common]<br>constructor(annotation: [Annotation](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-annotation/index.html), source: [KClass](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.reflect/-k-class/index.html)&lt;*&gt;?) |

## Properties

| Name | Summary |
|---|---|
| [annotation](annotation.md) | [common]<br>val [annotation](annotation.md): [Annotation](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-annotation/index.html) |
| [source](source.md) | [common]<br>val [source](source.md): [KClass](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.reflect/-k-class/index.html)&lt;*&gt;?<br>The class this annotation was declared on. If null, it was declared on the current element. |
