//[inspekt](../../../index.md)/[dev.rnett.inspekt.model](../index.md)/[Callable](index.md)

# Callable

sealed class [Callable](index.md) : [AnnotatedElement](../-annotated-element/index.md)

The result of inspekting a function-like or property declaration.

#### Inheritors

| |
|---|
| [FunctionLike](../-function-like/index.md) |
| [Property](../-property/index.md) |

## Properties

| Name | Summary |
|---|---|
| [annotations](annotations.md) | [common]<br>override val [annotations](annotations.md): [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Annotation](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-annotation/index.html)&gt; |
| [inheritedFrom](inherited-from.md) | [common]<br>val [inheritedFrom](inherited-from.md): [KClass](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.reflect/-k-class/index.html)&lt;*&gt;?<br>If the declaration is inherited from a superclass (**not** overridden), the superclass it is inherited from. |
| [isAbstract](is-abstract.md) | [common]<br>val [isAbstract](is-abstract.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)<br>Whether the declaration is abstract. If checking whether you can invoke a function, use [FunctionLike.isInvokable](../-function-like/is-invokable.md) instead of this. |
| [isDeclared](is-declared.md) | [common]<br>val [isDeclared](is-declared.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)<br>Whether the declaration is declared in its owning class. False if it is inherited. |
| [isTopLevel](is-top-level.md) | [common]<br>val [isTopLevel](is-top-level.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)<br>Whether the declaration is top-level (not a member of a class). |
| [kotlin](kotlin.md) | [common]<br>abstract val [kotlin](kotlin.md): [KCallable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.reflect/-k-callable/index.html)&lt;*&gt;<br>A reference to the Kotlin declaration this represents. |
| [name](name.md) | [common]<br>abstract val [name](name.md): [CallableName](../../dev.rnett.inspekt.model.name/-callable-name/index.md)<br>The fully qualified name of the declaration. |
| [parameters](parameters.md) | [common]<br>val [parameters](parameters.md): [Parameters](../-parameters/index.md)<br>The parameters of the declaration. |
| [shortName](short-name.md) | [common]<br>val [shortName](short-name.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)<br>The short name of the declaration. |

## Functions

| Name | Summary |
|---|---|
| [annotation](../annotation.md) | [common]<br>inline fun &lt;[T](../annotation.md)&gt; [AnnotatedElement](../-annotated-element/index.md).[annotation](../annotation.md)(): [T](../annotation.md)?<br>Get the first annotation of type [T](../annotation.md), or null if none is found. |
| [annotations](../annotations.md) | [common]<br>inline fun &lt;[T](../annotations.md)&gt; [AnnotatedElement](../-annotated-element/index.md).[annotations](../annotations.md)(): [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[T](../annotations.md)&gt;<br>Get all annotations of type [T](../annotations.md). |
| [buildArguments](../../dev.rnett.inspekt.model.arguments/build-arguments.md) | [common]<br>inline fun [Callable](index.md).[buildArguments](../../dev.rnett.inspekt.model.arguments/build-arguments.md)(builder: [ArgumentsBuilder](../../dev.rnett.inspekt.model.arguments/-arguments-builder/index.md)): [ArgumentList](../../dev.rnett.inspekt.model.arguments/-argument-list/index.md)<br>Builds an [ArgumentList](../../dev.rnett.inspekt.model.arguments/-argument-list/index.md) for a set of parameters. Parameters which are not set will use their default values if they have one. Successfully building an [ArgumentList](../../dev.rnett.inspekt.model.arguments/-argument-list/index.md) guarantees that values are provided for all parameters without default values. |
| [equals](equals.md) | [common]<br>open operator override fun [equals](equals.md)(other: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) |
| [hashCode](hash-code.md) | [common]<br>open override fun [hashCode](hash-code.md)(): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) |
| [toString](to-string.md) | [common]<br>override fun [toString](to-string.md)(): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)<br>abstract fun [toString](to-string.md)(includeFullName: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |
