//[inspekt](../../../index.md)/[dev.rnett.inspekt.model.name](../index.md)/[PackageName](index.md)

# PackageName

[common]\
data class [PackageName](index.md)(val packageNames: [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt;) : [QualifiedName](../-qualified-name/index.md)

A package name.

## Constructors

| | |
|---|---|
| [PackageName](-package-name.md) | [common]<br>constructor(packageNames: [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt;)constructor(vararg packageNames: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [isRoot](is-root.md) | [common]<br>val [isRoot](is-root.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)<br>True if this is the root package. |
| [packageNames](package-names.md) | [common]<br>val [packageNames](package-names.md): [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt; |

## Functions

| Name | Summary |
|---|---|
| [asString](../-qualified-name/as-string.md) | [common]<br>open fun [asString](../-qualified-name/as-string.md)(): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)<br>The name as a Kotlin name string (i.e. using `.` to separate the segments). |
| [child](child.md) | [common]<br>fun [child](child.md)(name: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)): [PackageName](index.md)<br>Create a child package name. |
| [className](class-name.md) | [common]<br>fun [className](class-name.md)(vararg names: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)): [ClassName](../-class-name/index.md)<br>Create a class name for a class located in this package, with [names](class-name.md) class names. |
| [member](member.md) | [common]<br>fun [member](member.md)(name: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)): [CallableName.TopLevel](../-callable-name/-top-level/index.md)<br>Create a top-level callable name located in this package, with the [name](member.md) simple name. |
| [segments](segments.md) | [common]<br>open override fun [segments](segments.md)(): [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt;<br>All segments of the name. |
| [toString](../-qualified-name/to-string.md) | [common]<br>override fun [toString](../-qualified-name/to-string.md)(): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |
