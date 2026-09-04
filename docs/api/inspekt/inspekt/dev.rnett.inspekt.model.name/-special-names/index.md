//[inspekt](../../../index.md)/[dev.rnett.inspekt.model.name](../index.md)/[SpecialNames](index.md)

# SpecialNames

[common]\
object [SpecialNames](index.md)

Names with special meaning.

## Properties

| Name | Summary |
|---|---|
| [constructor](constructor.md) | [common]<br>const val [constructor](constructor.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)<br>The name of a constructor. |
| [receiver](receiver.md) | [common]<br>const val [receiver](receiver.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)<br>The name of a receiver parameter. |

## Functions

| Name | Summary |
|---|---|
| [getter](getter.md) | [common]<br>fun [getter](getter.md)(property: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)<br>The name of a getter for a property named [property](getter.md). |
| [propertyForGetter](property-for-getter.md) | [common]<br>fun [propertyForGetter](property-for-getter.md)(getterName: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?<br>Gets the property name for a getter named [getterName](property-for-getter.md), or `null` if [getterName](property-for-getter.md) is not the name of a getter. |
| [propertyForSetter](property-for-setter.md) | [common]<br>fun [propertyForSetter](property-for-setter.md)(setterName: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)?<br>Gets the property name for a setter named [setterName](property-for-setter.md), or `null` if [setterName](property-for-setter.md) is not the name of a setter. |
| [setter](setter.md) | [common]<br>fun [setter](setter.md)(property: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)<br>The name of a setter for a property named [property](setter.md). |
