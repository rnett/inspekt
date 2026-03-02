//[inspekt](../../../index.md)/[dev.rnett.inspekt.model](../index.md)/[Function](index.md)

# Function

sealed class [Function](index.md) : [FunctionLike](../-function-like/index.md)

The result of inspekting a function-like declaration that is a normal function, not a constructor.

#### Inheritors

| |
|---|
| [SimpleFunction](../-simple-function/index.md) |
| [PropertyAccessor](../-property-accessor/index.md) |

## Properties

| Name | Summary |
|---|---|
| [annotations](../-callable/annotations.md) | [common]<br>override val [annotations](../-callable/annotations.md): [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Annotation](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-annotation/index.html)&gt; |
| [inheritedFrom](../-callable/inherited-from.md) | [common]<br>val [inheritedFrom](../-callable/inherited-from.md): [KClass](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.reflect/-k-class/index.html)&lt;*&gt;?<br>If the declaration is inherited from a superclass (**not** overridden), the superclass it is inherited from. |
| [isAbstract](../-callable/is-abstract.md) | [common]<br>val [isAbstract](../-callable/is-abstract.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)<br>Whether the declaration is abstract. If checking whether you can invoke a function, use [FunctionLike.isInvokable](../-function-like/is-invokable.md) instead of this. |
| [isDeclared](../-callable/is-declared.md) | [common]<br>val [isDeclared](../-callable/is-declared.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)<br>Whether the declaration is declared in its owning class. False if it is inherited. |
| [isInvokable](../-function-like/is-invokable.md) | [common]<br>open val [isInvokable](../-function-like/is-invokable.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)<br>Whether the method can be invoked. **Almost** always equal to `!isAbstract`, but may be false if conditions (such as `reified` type parameters) prevent generating an invoker lambda. |
| [isSuspend](../-function-like/is-suspend.md) | [common]<br>val [isSuspend](../-function-like/is-suspend.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)<br>Whether the referenced function is suspending. If true, calling [invoke](../-function-like/invoke.md) will error - use [invokeSuspend](../-function-like/invoke-suspend.md) instead. |
| [isTopLevel](../-callable/is-top-level.md) | [common]<br>val [isTopLevel](../-callable/is-top-level.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)<br>Whether the declaration is top-level (not a member of a class). |
| [kotlin](../-function-like/kotlin.md) | [common]<br>abstract override val [kotlin](../-function-like/kotlin.md): [KFunction](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.reflect/-k-function/index.html)&lt;*&gt;<br>A reference to the Kotlin declaration this represents. |
| [name](../-function-like/name.md) | [common]<br>abstract override val [name](../-function-like/name.md): [CallableName](../../dev.rnett.inspekt.model.name/-callable-name/index.md)<br>The fully qualified name of the declaration. |
| [parameters](../-callable/parameters.md) | [common]<br>val [parameters](../-callable/parameters.md): [Parameters](../-parameters/index.md)<br>The parameters of the declaration. |
| [returnType](../-function-like/return-type.md) | [common]<br>val [returnType](../-function-like/return-type.md): [KType](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.reflect/-k-type/index.html)<br>The return type of the function. |
| [shortName](../-callable/short-name.md) | [common]<br>val [shortName](../-callable/short-name.md): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)<br>The short name of the declaration. |

## Functions

| Name | Summary |
|---|---|
| [annotation](../annotation.md) | [common]<br>inline fun &lt;[T](../annotation.md)&gt; [AnnotatedElement](../-annotated-element/index.md).[annotation](../annotation.md)(): [T](../annotation.md)?<br>Get the first annotation of type [T](../annotation.md), or null if none is found. |
| [annotations](../annotations.md) | [common]<br>inline fun &lt;[T](../annotations.md)&gt; [AnnotatedElement](../-annotated-element/index.md).[annotations](../annotations.md)(): [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[T](../annotations.md)&gt;<br>Get all annotations of type [T](../annotations.md). |
| [buildArguments](../../dev.rnett.inspekt.model.arguments/build-arguments.md) | [common]<br>inline fun [Callable](../-callable/index.md).[buildArguments](../../dev.rnett.inspekt.model.arguments/build-arguments.md)(builder: [ArgumentsBuilder](../../dev.rnett.inspekt.model.arguments/-arguments-builder/index.md)): [ArgumentList](../../dev.rnett.inspekt.model.arguments/-argument-list/index.md)<br>Builds an [ArgumentList](../../dev.rnett.inspekt.model.arguments/-argument-list/index.md) for a set of parameters. Parameters which are not set will use their default values if they have one. Successfully building an [ArgumentList](../../dev.rnett.inspekt.model.arguments/-argument-list/index.md) guarantees that values are provided for all parameters without default values. |
| [equals](../-callable/equals.md) | [common]<br>open operator override fun [equals](../-callable/equals.md)(other: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html) |
| [hashCode](../-callable/hash-code.md) | [common]<br>open override fun [hashCode](../-callable/hash-code.md)(): [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html) |
| [invoke](../-function-like/invoke.md) | [common]<br>operator fun [invoke](../-function-like/invoke.md)(arguments: [ArgumentList](../../dev.rnett.inspekt.model.arguments/-argument-list/index.md)): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?<br>inline operator fun [invoke](../-function-like/invoke.md)(arguments: [ArgumentsBuilder](../../dev.rnett.inspekt.model.arguments/-arguments-builder/index.md)): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?<br>Invoke the referenced function with the given parameters. Will error if the underlying function is `suspend`. |
| [invokeSuspend](../-function-like/invoke-suspend.md) | [common]<br>suspend fun [invokeSuspend](../-function-like/invoke-suspend.md)(arguments: [ArgumentList](../../dev.rnett.inspekt.model.arguments/-argument-list/index.md)): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?<br>inline suspend fun [invokeSuspend](../-function-like/invoke-suspend.md)(arguments: [ArgumentsBuilder](../../dev.rnett.inspekt.model.arguments/-arguments-builder/index.md)): [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)?<br>Invoke the referenced `suspend` function with the given parameters. If the function is not suspend, this will still invoke it without an error. |
| [toString](../-callable/to-string.md) | [common]<br>override fun [toString](../-callable/to-string.md)(): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html)<br>abstract fun [toString](../-callable/to-string.md)(includeFullName: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)): [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html) |
