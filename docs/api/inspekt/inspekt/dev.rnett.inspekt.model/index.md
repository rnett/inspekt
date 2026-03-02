//[inspekt](../../index.md)/[dev.rnett.inspekt.model](index.md)

# Package-level declarations

[common]\
Data models for Kotlin declarations.

## Types

| Name | Summary |
|---|---|
| [AnnotatedElement](-annotated-element/index.md) | [common]<br>interface [AnnotatedElement](-annotated-element/index.md)<br>An element which has annotations. |
| [Callable](-callable/index.md) | [common]<br>sealed class [Callable](-callable/index.md) : [AnnotatedElement](-annotated-element/index.md)<br>The result of inspekting a function-like or property declaration. |
| [Constructor](-constructor/index.md) | [common]<br>class [Constructor](-constructor/index.md) : [FunctionLike](-function-like/index.md) |
| [Function](-function/index.md) | [common]<br>sealed class [Function](-function/index.md) : [FunctionLike](-function-like/index.md)<br>The result of inspekting a function-like declaration that is a normal function, not a constructor. |
| [FunctionLike](-function-like/index.md) | [common]<br>sealed class [FunctionLike](-function-like/index.md) : [Callable](-callable/index.md)<br>The result of inspekting a member that can be invoked like a function. Includes functions, constructors, and accessors. |
| [Inspektion](-inspektion/index.md) | [common]<br>class [Inspektion](-inspektion/index.md)&lt;[T](-inspektion/index.md) : [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)&gt; : [AnnotatedElement](-annotated-element/index.md)<br>The result of inspekting a class. |
| [MutableProperty](-mutable-property/index.md) | [common]<br>class [MutableProperty](-mutable-property/index.md) : [Property](-property/index.md)<br>The result of inspekting a `var` property. |
| [Parameter](-parameter/index.md) | [common]<br>data class [Parameter](-parameter/index.md) : [AnnotatedElement](-annotated-element/index.md)<br>A parameter of a function or other callable declaration. |
| [Parameters](-parameters/index.md) | [common]<br>data class [Parameters](-parameters/index.md) : [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Parameter](-parameter/index.md)&gt; <br>The parameters of a callable. Parameters are ordered by kind ([Parameter.Kind](-parameter/-kind/index.md)): |
| [Property](-property/index.md) | [common]<br>sealed class [Property](-property/index.md) : [Callable](-callable/index.md)<br>The result of inspekting a property. |
| [PropertyAccessor](-property-accessor/index.md) | [common]<br>sealed class [PropertyAccessor](-property-accessor/index.md) : [Function](-function/index.md)<br>The result of inspekting a property accessor. |
| [PropertyGetter](-property-getter/index.md) | [common]<br>class [PropertyGetter](-property-getter/index.md) : [PropertyAccessor](-property-accessor/index.md)<br>The result of inspekting property getter. |
| [PropertySetter](-property-setter/index.md) | [common]<br>class [PropertySetter](-property-setter/index.md) : [PropertyAccessor](-property-accessor/index.md)<br>The result of inspekting property setter. |
| [ReadOnlyProperty](-read-only-property/index.md) | [common]<br>class [ReadOnlyProperty](-read-only-property/index.md) : [Property](-property/index.md)<br>The result of inspekting a `val` property. |
| [SimpleFunction](-simple-function/index.md) | [common]<br>class [SimpleFunction](-simple-function/index.md) : [Function](-function/index.md)<br>The result of inspekting simple function declaration, i.e. one declared with `fun`. |
| [TypeParameter](-type-parameter/index.md) | [common]<br>data class [TypeParameter](-type-parameter/index.md)(val name: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), val index: [Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html), val variance: [TypeParameter.Variance](-type-parameter/-variance/index.md), val erasedUpperBounds: [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[KType](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.reflect/-k-type/index.html)&gt;, val isReified: [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html), val annotations: [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[Annotation](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-annotation/index.html)&gt;) : [AnnotatedElement](-annotated-element/index.md)<br>A type parameter. |

## Functions

| Name | Summary |
|---|---|
| [annotation](annotation.md) | [common]<br>inline fun &lt;[T](annotation.md)&gt; [AnnotatedElement](-annotated-element/index.md).[annotation](annotation.md)(): [T](annotation.md)?<br>Get the first annotation of type [T](annotation.md), or null if none is found. |
| [annotations](annotations.md) | [common]<br>inline fun &lt;[T](annotations.md)&gt; [AnnotatedElement](-annotated-element/index.md).[annotations](annotations.md)(): [List](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[T](annotations.md)&gt;<br>Get all annotations of type [T](annotations.md). |
