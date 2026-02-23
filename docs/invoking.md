# Invoking Functions

Inspekt allows invoking functions that have been inspekted, including property accessors.

Of course, this only works if the function has a body that can be invoked.
If you are inspekting a concrete class, this is guaranteed.
But if the inspektion is of an abstract class, such as the interface for a proxy, some members may not be invokable.
To check this, check the `isInvokable` property.
Functions with `reified` type parameters are also not invokable, so it is important to use `isInvokable` instead of `isAbstract`.
You may also want to check `isSuspend` - attempting to invoke a suspending function using the `invoke` method (instead of `invokeSuspend`) will throw an error.

`Inspektion` also provides convenient shorthand utilities for invoking and accessing non-ambiguous member functions and properties, see [Inspektion](./inspektion.md).

## The Invoke function

The most common way to call a function is using the `invoke` method. It uses a lambda block to configure the arguments.

```kotlin
val foo = Foo()
val result = inspekt(Foo::bar).invoke {
    dispatchReceiver = foo
    value(arg1, arg2)
}
```

### Arguments

All arguments are stored in a flat list matching the parameter order.
The ordering of argument and parameter kinds matches that used by the Kotlin compiler:

* Dispatch receiver (if present)
* Context parameters
* Extension receiver (if present)
* Value parameters

The `invoke` lambda's receiver, `ArgumentList.Builder`, provides many utilities for setting arguments:

* `dispatchReceiver`
* `extensionReceiver`
* `this[Kind, index] = value`
* `context(context1, context2, ...)`
* `value(value1, value2, ...)`
* And more – see the API documentation.

To use the default value of an argument, do not set it.
Setting an argument to `null` actually passes `null` and will not use the default.

Some limited type checking is done for arguments before calling the method, but it is limited to what is available at runtime using `KType`.

## Suspend Functions

Invoking `suspend` functions is just as easy, but you must use the `invokeSuspend` method within a coroutine or another `suspend` function:

```kotlin
suspend fun example() {
    val result = inspekt(Foo::asyncTask).invokeSuspend {
        dispatchReceiver = Foo()
        value("some data")
    }
}
```

Calling `invoke` on a `suspend` function will throw an exception.
Calling `invokeSuspend` on a non-suspend function will work fine, and just call the non-suspending invoker.

## Properties

You can also get and set property values using a similar syntax:

```kotlin
val value = prop.get { dispatchReceiver = foo }
prop.set {
    dispatchReceiver = foo
    value(newValue)
}
```

or by using `invoke` on their getter or setter:

```kotlin
prop.getter.invoke { dispatchReceiver = foo }
```