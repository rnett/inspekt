//[inspekt](../../../index.md)/[dev.rnett.inspekt.model.name](../index.md)/[CallableName](index.md)

# CallableName

sealed class [CallableName](index.md) : [QualifiedName](../-qualified-name/index.md)

The qualified name of a callable declaration, which may either be top level or the member of a class.

#### Inheritors

| |
|---|
| [TopLevel](-top-level/index.md) |
| [Member](-member/index.md) |

## Types

| Name | Summary |
|---|---|
| [Member](-member/index.md) | [common]<br>data class [Member](-member/index.md)(val className: [ClassName](../-class-name/index.md), val name: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)) : [CallableName](index.md)<br>The qualified name of a member of a class. |
| [TopLevel](-top-level/index.md) | [common]<br>data class [TopLevel](-top-level/index.md)(val packageName: [PackageName](../-package-name/index.md), val name: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)) : [CallableName](index.md)<br>A top-level callable's qualified name. |

## Properties

| Name | Summary |
|---|---|
| [isPropertyAccessor](is-property-accessor.md) | [common]<br>val [isPropertyAccessor](is-property-accessor.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)<br>Whether this is the name of a property accessor. |
| [isPropertyGetter](is-property-getter.md) | [common]<br>val [isPropertyGetter](is-property-getter.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)<br>Whether this is the name of a property getter. |
| [isPropertySetter](is-property-setter.md) | [common]<br>val [isPropertySetter](is-property-setter.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)<br>Whether this is the name of a property setter. |
| [name](name.md) | [common]<br>abstract val [name](name.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)<br>The simple, non-qualified name of the declaration. |
| [packageName](package-name.md) | [common]<br>abstract val [packageName](package-name.md): [PackageName](../-package-name/index.md)<br>The name of the package that contains this declaration or its parent class. |
| [propertyIfAccessor](property-if-accessor.md) | [common]<br>open val [propertyIfAccessor](property-if-accessor.md): [CallableName](index.md)?<br>Get the corresponding property name if this is the name of a property accessor. |

## Functions

| Name | Summary |
|---|---|
| [asString](../-qualified-name/as-string.md) | [common]<br>open fun [asString](../-qualified-name/as-string.md)(): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)<br>The name as a Kotlin name string (i.e. using `.` to separate the segments). |
| [segments](../-qualified-name/segments.md) | [common]<br>abstract fun [segments](../-qualified-name/segments.md)(): [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt;<br>All segments of the name. |
| [sibling](sibling.md) | [common]<br>abstract fun [sibling](sibling.md)(name: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)): [CallableName](index.md)<br>Create a sibling callable name with the [name](sibling.md) simple name. |
| [toString](../-qualified-name/to-string.md) | [common]<br>override fun [toString](../-qualified-name/to-string.md)(): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |
