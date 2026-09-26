//[inspekt](../../../index.md)/[dev.rnett.inspekt.model.name](../index.md)/[QualifiedName](index.md)

# QualifiedName

abstract class [QualifiedName](index.md)

A fully qualified name of a declaration.

#### Inheritors

| |
|---|
| [PackageName](../-package-name/index.md) |
| [ClassName](../-class-name/index.md) |
| [CallableName](../-callable-name/index.md) |

## Constructors

| | |
|---|---|
| [QualifiedName](-qualified-name.md) | [common]<br>constructor() |

## Functions

| Name | Summary |
|---|---|
| [asString](as-string.md) | [common]<br>open fun [asString](as-string.md)(): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)<br>The name as a Kotlin name string (i.e. using `.` to separate the segments). |
| [segments](segments.md) | [common]<br>abstract fun [segments](segments.md)(): [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt;<br>All segments of the name. |
| [toString](to-string.md) | [common]<br>override fun [toString](to-string.md)(): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |
