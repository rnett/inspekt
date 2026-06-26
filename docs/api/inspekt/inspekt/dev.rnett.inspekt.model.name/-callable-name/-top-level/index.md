//[inspekt](../../../../index.md)/[dev.rnett.inspekt.model.name](../../index.md)/[CallableName](../index.md)/[TopLevel](index.md)

# TopLevel

[common]\
data class [TopLevel](index.md)(val packageName: [PackageName](../../-package-name/index.md), val name: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)) : [CallableName](../index.md)

A top-level callable's qualified name.

## Constructors

| | |
|---|---|
| [TopLevel](-top-level.md) | [common]<br>constructor(packageName: [PackageName](../../-package-name/index.md), name: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [isPropertyAccessor](../is-property-accessor.md) | [common]<br>val [isPropertyAccessor](../is-property-accessor.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)<br>Whether this is the name of a property accessor. |
| [isPropertyGetter](../is-property-getter.md) | [common]<br>val [isPropertyGetter](../is-property-getter.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)<br>Whether this is the name of a property getter. |
| [isPropertySetter](../is-property-setter.md) | [common]<br>val [isPropertySetter](../is-property-setter.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)<br>Whether this is the name of a property setter. |
| [name](name.md) | [common]<br>open override val [name](name.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)<br>The declaration's simple name. |
| [packageName](package-name.md) | [common]<br>open override val [packageName](package-name.md): [PackageName](../../-package-name/index.md)<br>The declaration's package. |
| [propertyIfAccessor](property-if-accessor.md) | [common]<br>open override val [propertyIfAccessor](property-if-accessor.md): [CallableName.TopLevel](index.md)?<br>Get the corresponding property name if this is the name of a property accessor. |

## Functions

| Name | Summary |
|---|---|
| [asString](as-string.md) | [common]<br>open override fun [asString](as-string.md)(): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)<br>The name as a Kotlin name string (i.e. using `.` to separate the segments). |
| [segments](segments.md) | [common]<br>open override fun [segments](segments.md)(): [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)&gt;<br>All segments of the name. |
| [sibling](sibling.md) | [common]<br>open override fun [sibling](sibling.md)(name: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)): [CallableName.TopLevel](index.md)<br>Create a sibling callable name with the [name](../sibling.md) simple name. |
| [toString](../../-qualified-name/to-string.md) | [common]<br>override fun [toString](../../-qualified-name/to-string.md)(): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |
