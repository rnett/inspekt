//[inspekt](../../../index.md)/[dev.rnett.inspekt.model](../index.md)/[Inspektion](index.md)/[callPrimaryConstructor](call-primary-constructor.md)

# callPrimaryConstructor

[common]\
inline fun [callPrimaryConstructor](call-primary-constructor.md)(argumentsBuilder: [ArgumentsBuilder](../../dev.rnett.inspekt.model.arguments/-arguments-builder/index.md) = {}): [T](index.md)

Calls the primary constructor of this class with [argumentsBuilder](call-primary-constructor.md).

#### See also

| |
|---|
| [FunctionLike.invoke](../-function-like/invoke.md) |
| [Inspektion.primaryConstructor](primary-constructor.md) |

#### Throws

| | |
|---|---|
| [FunctionInvocationException](../../dev.rnett.inspekt.exceptions/-function-invocation-exception/index.md) | if the class has no primary constructor or it cannot be invoked. |
