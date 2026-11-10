//[gradle-plugin](../../../index.md)/[dev.rnett.inspekt](../index.md)/[InspektExtension](index.md)/[warnOnDefaultParameters](warn-on-default-parameters.md)

# warnOnDefaultParameters

[jvm]\
abstract val [warnOnDefaultParameters](warn-on-default-parameters.md): Property&lt;[Int](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-int/index.html)&gt;

If greater than 0 (the default is 5), warns when invocation lambas are generated for functions with more default arguments than this number. This is because the invocation lambda has a when statement with a branch for each combination of default arguments, meaning `2 ^ (number of default arguments)` cases. This can easily lead to performance issues and binary size bloat.
