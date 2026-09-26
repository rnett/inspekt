//[inspekt](../../../index.md)/[dev.rnett.inspekt.model](../index.md)/[FunctionLike](index.md)/[isInvokable](is-invokable.md)

# isInvokable

[common]\
open val [isInvokable](is-invokable.md): [Boolean](https://kotlinlang.org/api/core/kotlin-stdlib/kotlin/-boolean/index.html)

Whether the method can be invoked. **Almost** always equal to `!isAbstract`, but may be false if conditions (such as `reified` type parameters) prevent generating an invoker lambda.
