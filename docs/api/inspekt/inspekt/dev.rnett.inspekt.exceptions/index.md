//[inspekt](../../index.md)/[dev.rnett.inspekt.exceptions](index.md)

# Package-level declarations

[common]\
Exceptions and error markers.

## Types

| Name | Summary |
|---|---|
| [FunctionInvocationException](-function-invocation-exception/index.md) | [common]<br>class [FunctionInvocationException](-function-invocation-exception/index.md)(val name: [QualifiedName](../dev.rnett.inspekt.model.name/-qualified-name/index.md), val cause: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html)) : [RuntimeException](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-runtime-exception/index.html)<br>Exception thrown when a function invocation fails. |
| [InspektCompilerPluginIntrinsic](-inspekt-compiler-plugin-intrinsic/index.md) | [common]<br>annotation class [InspektCompilerPluginIntrinsic](-inspekt-compiler-plugin-intrinsic/index.md)<br>Marks declarations that must be replaced by the Inspekt compiler plugin. The Inspekt Gradle plugin automatically opts-in to this annotation. If you are seeing it, it likely means that the declaration will not be replaced with your current build configuration, resulting in runtime errors |
| [InspektNotIntrinsifiedException](-inspekt-not-intrinsified-exception/index.md) | [common]<br>class [InspektNotIntrinsifiedException](-inspekt-not-intrinsified-exception/index.md) : [UnsupportedOperationException](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-unsupported-operation-exception/index.html)<br>Thrown when a declaration marked with [InspektCompilerPluginIntrinsic](-inspekt-compiler-plugin-intrinsic/index.md) is not replaced by the Inspekt compiler plugin. |
| [ProxyInvocationException](-proxy-invocation-exception/index.md) | [common]<br>class [ProxyInvocationException](-proxy-invocation-exception/index.md)(val name: [String](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-string/index.html), val proxyHandler: [ProxyHandler](../dev.rnett.inspekt.proxy/-proxy-handler/index.md), val proxyInstance: [Any](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-any/index.html), cause: [Throwable](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-throwable/index.html)) : [RuntimeException](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-runtime-exception/index.html)<br>Exception thrown when a function invocation on a proxy fails. |
