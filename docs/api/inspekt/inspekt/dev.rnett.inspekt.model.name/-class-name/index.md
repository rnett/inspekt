//[inspekt](../../../index.md)/[dev.rnett.inspekt.model.name](../index.md)/[ClassName](index.md)

# ClassName

[common]\
data class [ClassName](index.md)(val packageName: [PackageName](../-package-name/index.md), val classNames: [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt;) : [QualifiedName](../-qualified-name/index.md)

The qualified name of a class.

For example, a class `B` like

```kotlin
package com.example
class A { class B {} }
```

would have a name like `ClassName(["com", "example"], ["A", "B"])`.

## Constructors

| | |
|---|---|
| [ClassName](-class-name.md) | [common]<br>constructor(packageName: [PackageName](../-package-name/index.md), classNames: [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt;) |

## Properties

| Name | Summary |
|---|---|
| [classNames](class-names.md) | [common]<br>val [classNames](class-names.md): [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt;<br>The class's name, and any parent classes. For example, a class `B` like |
| [packageName](package-name.md) | [common]<br>val [packageName](package-name.md): [PackageName](../-package-name/index.md)<br>The class's package. |
| [simpleName](simple-name.md) | [common]<br>val [simpleName](simple-name.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)<br>The simple name of the class. Typically the class's own, non-qualified name. |

## Functions

| Name | Summary |
|---|---|
| [asString](as-string.md) | [common]<br>open override fun [asString](as-string.md)(): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)<br>The name as a Kotlin name string (i.e. using `.` to separate the segments). |
| [child](child.md) | [common]<br>fun [child](child.md)(name: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)): [ClassName](index.md)<br>Create a child class name with the [name](child.md) simple name. |
| [member](member.md) | [common]<br>fun [member](member.md)(name: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)): [CallableName.Member](../-callable-name/-member/index.md)<br>Create a member callable name with the [name](member.md) simple name. |
| [segments](segments.md) | [common]<br>open override fun [segments](segments.md)(): [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt;<br>All segments of the name. |
| [toString](../-qualified-name/to-string.md) | [common]<br>override fun [toString](../-qualified-name/to-string.md)(): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |
