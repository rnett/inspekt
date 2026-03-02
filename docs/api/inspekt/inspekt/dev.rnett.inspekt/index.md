//[inspekt](../../index.md)/[dev.rnett.inspekt](index.md)

# Package-level declarations

[common]\
Inspektion creation methods.

The values these methods return are created when the method is called, without any caching - a call to this method is transformed into a [Inspektion](../dev.rnett.inspekt.model/-inspektion/index.md) (or function or property model) constructor call by the compiler plugin. All the arguments are calculated at compilation time.

Because of this, uses of this function can potentially add a significant amount of binary size. This is based on the number of appearances of `inspekt()` in your code and how many members and parameters the argument has, not how many times it is invoked.

Calling this function on a declaration that is or contains functions that have a large number of default parameters can result in inefficient invoke methods and significantly more binary size bloat. A compiler warning will be shown in this case.

## Functions

| Name | Summary |
|---|---|
| [inspekt](inspekt.md) | [common]<br>@[InspektCompilerPluginIntrinsic](../dev.rnett.inspekt.exceptions/-inspekt-compiler-plugin-intrinsic/index.md)<br>fun &lt;[T](inspekt.md) : [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html)&gt; [inspekt](inspekt.md)(@[ReferenceLiteral](../../../inspekt/dev.rnett.inspekt.utils/-reference-literal/index.md)kClass: [KClass](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.reflect/-k-class/index.html)&lt;[T](inspekt.md)&gt;): [Inspektion](../dev.rnett.inspekt.model/-inspektion/index.md)&lt;[T](inspekt.md)&gt;<br>Creates an [dev.rnett.inspekt.model.Inspektion](../dev.rnett.inspekt.model/-inspektion/index.md) for [kClass](inspekt.md), which must be a class reference literal. It will contain all members that are visible from the call site.<br>[common]<br>@[InspektCompilerPluginIntrinsic](../dev.rnett.inspekt.exceptions/-inspekt-compiler-plugin-intrinsic/index.md)<br>fun [inspekt](inspekt.md)(@[ReferenceLiteral](../../../inspekt/dev.rnett.inspekt.utils/-reference-literal/index.md)function: [KFunction](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.reflect/-k-function/index.html)&lt;*&gt;): [SimpleFunction](../dev.rnett.inspekt.model/-simple-function/index.md)<br>Creates an [dev.rnett.inspekt.model.SimpleFunction](../dev.rnett.inspekt.model/-simple-function/index.md) for [function](inspekt.md), which must be a function reference literal. Instance function references (e.g. `foo::bar`) and class function references (e.g. `Foo::bar`) are handled the same - as class function references.<br>[common]<br>@[InspektCompilerPluginIntrinsic](../dev.rnett.inspekt.exceptions/-inspekt-compiler-plugin-intrinsic/index.md)<br>fun [inspekt](inspekt.md)(@[ReferenceLiteral](../../../inspekt/dev.rnett.inspekt.utils/-reference-literal/index.md)property: [KProperty](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.reflect/-k-property/index.html)&lt;*&gt;): [Property](../dev.rnett.inspekt.model/-property/index.md)<br>Creates an [dev.rnett.inspekt.model.Property](../dev.rnett.inspekt.model/-property/index.md) for [property](inspekt.md), which must be a property reference literal. Instance property references (e.g. `foo::bar`) and class property references (e.g. `Foo::bar`) are handled the same - as class property references. |
