//[inspekt](../../../index.md)/[dev.rnett.inspekt.model](../index.md)/[MutableProperty](index.md)

# MutableProperty

[common]\
class [MutableProperty](index.md) : [Property](../-property/index.md)

The result of inspekting a `var` property.

## Properties

| Name | Summary |
|---|---|
| [annotations](../-callable/annotations.md) | [common]<br>override val [annotations](../-callable/annotations.md): [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Annotation](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-annotation/index.html)&gt; |
| [getter](../-property/getter.md) | [common]<br>val [getter](../-property/getter.md): [PropertyGetter](../-property-getter/index.md)<br>The property's getter. |
| [hasBackingField](../-property/has-backing-field.md) | [common]<br>val [hasBackingField](../-property/has-backing-field.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)<br>Whether the property has a backing field. |
| [hasDelegate](../-property/has-delegate.md) | [common]<br>val [hasDelegate](../-property/has-delegate.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)<br>Whether the property has a delegate. |
| [inheritedFrom](../-callable/inherited-from.md) | [common]<br>val [inheritedFrom](../-callable/inherited-from.md): [KClass](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.reflect/-k-class/index.html)&lt;*&gt;?<br>If the declaration is inherited from a superclass (**not** overridden), the superclass it is inherited from. |
| [isAbstract](../-callable/is-abstract.md) | [common]<br>val [isAbstract](../-callable/is-abstract.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)<br>Whether the declaration is abstract. If checking whether you can invoke a function, use [FunctionLike.isInvokable](../-function-like/is-invokable.md) instead of this. |
| [isDeclared](../-callable/is-declared.md) | [common]<br>val [isDeclared](../-callable/is-declared.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)<br>Whether the declaration is declared in its owning class. False if it is inherited. |
| [isTopLevel](../-callable/is-top-level.md) | [common]<br>val [isTopLevel](../-callable/is-top-level.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)<br>Whether the declaration is top-level (not a member of a class). |
| [kotlin](kotlin.md) | [common]<br>open override val [kotlin](kotlin.md): [KMutableProperty1](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.reflect/-k-mutable-property1/index.html)&lt;*, *&gt;<br>A reference to the Kotlin declaration this represents. |
| [name](../-property/name.md) | [common]<br>open override val [name](../-property/name.md): [CallableName](../../dev.rnett.inspekt.model.name/-callable-name/index.md)<br>The fully qualified name of the declaration. |
| [parameters](../-callable/parameters.md) | [common]<br>val [parameters](../-callable/parameters.md): [Parameters](../-parameters/index.md)<br>The parameters of the declaration. |
| [setter](setter.md) | [common]<br>open override val [setter](setter.md): [PropertySetter](../-property-setter/index.md)<br>The property's setter, if it has one. |
| [shortName](../-callable/short-name.md) | [common]<br>val [shortName](../-callable/short-name.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)<br>The short name of the declaration. |
| [type](../-property/type.md) | [common]<br>val [type](../-property/type.md): [KType](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.reflect/-k-type/index.html)<br>The type of the property. |

## Functions

| Name | Summary |
|---|---|
| [annotation](../annotation.md) | [common]<br>inline fun &lt;[T](../annotation.md)&gt; [AnnotatedElement](../-annotated-element/index.md).[annotation](../annotation.md)(): [T](../annotation.md)?<br>Get the first annotation of type [T](../annotation.md), or null if none is found. |
| [annotations](../annotations.md) | [common]<br>inline fun &lt;[T](../annotations.md)&gt; [AnnotatedElement](../-annotated-element/index.md).[annotations](../annotations.md)(): [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[T](../annotations.md)&gt;<br>Get all annotations of type [T](../annotations.md). |
| [buildArguments](../../dev.rnett.inspekt.model.arguments/build-arguments.md) | [common]<br>inline fun [Callable](../-callable/index.md).[buildArguments](../../dev.rnett.inspekt.model.arguments/build-arguments.md)(builder: [ArgumentsBuilder](../../dev.rnett.inspekt.model.arguments/-arguments-builder/index.md)): [ArgumentList](../../dev.rnett.inspekt.model.arguments/-argument-list/index.md)<br>Builds an [ArgumentList](../../dev.rnett.inspekt.model.arguments/-argument-list/index.md) for a set of parameters. Parameters which are not set will use their default values if they have one. Successfully building an [ArgumentList](../../dev.rnett.inspekt.model.arguments/-argument-list/index.md) guarantees that values are provided for all parameters without default values. |
| [equals](../-callable/equals.md) | [common]<br>open operator override fun [equals](../-callable/equals.md)(other: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) |
| [get](../-property/get.md) | [common]<br>fun [get](../-property/get.md)(arguments: [ArgumentList](../../dev.rnett.inspekt.model.arguments/-argument-list/index.md)): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?<br>Gets the value of a property by calling its getter.<br>[common]<br>fun [get](../-property/get.md)(arguments: [ArgumentsBuilder](../../dev.rnett.inspekt.model.arguments/-arguments-builder/index.md)): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?<br>Get the value of a property by calling its getter. |
| [hashCode](../-callable/hash-code.md) | [common]<br>open override fun [hashCode](../-callable/hash-code.md)(): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) |
| [set](set.md) | [common]<br>fun [set](set.md)(arguments: [ArgumentList](../../dev.rnett.inspekt.model.arguments/-argument-list/index.md)): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?<br>fun [set](set.md)(arguments: [ArgumentsBuilder](../../dev.rnett.inspekt.model.arguments/-arguments-builder/index.md)): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?<br>Set the value of a property by calling its setter. |
| [toString](../-callable/to-string.md) | [common]<br>override fun [toString](../-callable/to-string.md)(): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)<br>open override fun [toString](to-string.md)(includeFullName: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |
