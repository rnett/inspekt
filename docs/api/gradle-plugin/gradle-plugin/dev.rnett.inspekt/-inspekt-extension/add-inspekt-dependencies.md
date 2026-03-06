//[gradle-plugin](../../../index.md)/[dev.rnett.inspekt](../index.md)/[InspektExtension](index.md)/[addInspektDependencies](add-inspekt-dependencies.md)

# addInspektDependencies

[jvm]\
abstract val [addInspektDependencies](add-inspekt-dependencies.md): Property&lt;[Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)&gt;

If true (which is the default), the dependency on the `inspekt` runtime library will be automatically added to all source sets (as an `implementation` dependency). The library is required to invoke `inspekt`, so if it's not included, the compiler plugin is useless.
