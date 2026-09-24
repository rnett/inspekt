//[gradle-plugin](../../../index.md)/[dev.rnett.inspekt](../index.md)/[InspektExtension](index.md)

# InspektExtension

[jvm]\
abstract class [InspektExtension](index.md)

Configuration for the Inspekt Gradle plugin.

## Constructors

| | |
|---|---|
| [InspektExtension](-inspekt-extension.md) | [jvm]<br>constructor() |

## Properties

| Name | Summary |
|---|---|
| [addInspektDependencies](add-inspekt-dependencies.md) | [jvm]<br>abstract val [addInspektDependencies](add-inspekt-dependencies.md): Property&lt;[Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)&gt;<br>If true (which is the default), the dependency on the `inspekt` runtime library will be automatically added to all source sets (as an `implementation` dependency). The library is required to invoke `inspekt`, so if it's not included, the compiler plugin is useless. |
| [warnOnDefaultParameters](warn-on-default-parameters.md) | [jvm]<br>abstract val [warnOnDefaultParameters](warn-on-default-parameters.md): Property&lt;[Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)&gt;<br>If greater than 0 (the default is 5), warns when invocation lambas are generated for functions with more default arguments than this number. This is because the invocation lambda has a when statement with a branch for each combination of default arguments, meaning `2 ^ (number of default arguments)` cases. This can easily lead to performance issues and binary size bloat. |
