//[inspekt](../../index.md)/[dev.rnett.inspekt.model.name](index.md)

# Package-level declarations

[common]\
Declaration names.

## Types

| Name | Summary |
|---|---|
| [CallableName](-callable-name/index.md) | [common]<br>sealed class [CallableName](-callable-name/index.md) : [QualifiedName](-qualified-name/index.md)<br>The qualified name of a callable declaration, which may either be top level or the member of a class. |
| [ClassName](-class-name/index.md) | [common]<br>data class [ClassName](-class-name/index.md)(val packageName: [PackageName](-package-name/index.md), val classNames: [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt;) : [QualifiedName](-qualified-name/index.md)<br>The qualified name of a class. |
| [PackageName](-package-name/index.md) | [common]<br>data class [PackageName](-package-name/index.md)(val packageNames: [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt;) : [QualifiedName](-qualified-name/index.md)<br>A package name. |
| [QualifiedName](-qualified-name/index.md) | [common]<br>abstract class [QualifiedName](-qualified-name/index.md)<br>A fully qualified name of a declaration. |
| [SpecialNames](-special-names/index.md) | [common]<br>object [SpecialNames](-special-names/index.md)<br>Names with special meaning. |

## Functions

| Name | Summary |
|---|---|
| [CallableName](-callable-name.md) | [common]<br>fun [CallableName](-callable-name.md)(packageNames: [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt;, classNames: [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt;?, name: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)): [CallableName](-callable-name/index.md)<br>Constructs a callable name from the given package names, class names, and simple name. If [classNames](-callable-name.md) is null, the name is treated as top-level. |
