# Inspektion

The primary entry point for using Inspekt is the `inspekt()` function. It provides access to a comprehensive model of your code at runtime, without the overhead or restrictions of JVM reflection.

## Creating an Inspektion

To inspect a class, call `inspekt` with a class reference literal:

```kotlin
val fooInspektion = inspekt(Foo::class)
```

You can also inspect individual functions or properties:

```kotlin
val barFunction = inspekt(Foo::bar)
val bazProperty = inspekt(Foo::baz)
```

Calling `inspekt` on method references with receivers, e.g. `inspekt(foo::bar)` is the same as calling it on just `Foo::bar` - only the function or property referenced is relevant.

### Constraints

The `inspekt()` function is a compiler intrinsic. This means it has several constraints:

* **Literals only:** You must pass a class, function, or property reference literal (e.g., `Foo::class`, `Foo::bar`). You cannot pass a variable containing a `KClass` or `KFunction`, even if it's a constant.
* **Visibility:** You can only inspect declarations that are visible at the call site, and the inspektion will only contain members that are visible at the call site.
* **Compile-time:** The `Inspektion` object is constructed at compile-time. Each call to `inspekt()` generates a new instance in the compiled code. See [Binary Size](binary-size.md).

## The Data Model

An `Inspektion<T>` provides a detailed view of the class `T`, including:

* **Properties:** All properties, including those inherited from superclasses.
* **Functions:** All functions, including those inherited.
* **Constructors:** All available constructors.
* **Type Parameters:** Generic type information.
* **Annotations:** Access to annotations on the class and its members. Instances of the annotations are provided.
* **Hierarchy:** Supertypes and sealed subclasses.

**All** visible members of the class will be included, including those inherited by supertypes (e.g. a default `equals` implementation).
To determine where members were declared, use the `isDeclared` and `inheritedFrom` properties.

### Finding Members

You can look up members by name or iterate through the lists:

```kotlin
val prop = fooInspektion.property("myProperty")
val func = fooInspektion.function("myFunction")

val allPublicFunctions = fooInspektion.functions.filter { /* ... */ }
```

### Working with Objects

If the inspected class is a Kotlin `object`, you can access its instance directly:

```kotlin
val instance = inspekt(MyObject::class).objectInstance
```

Similarly, you can access the `companionObject` if one exists.

## Casting and Instance Checks

`Inspektion` provides multiplatform-safe alternatives to `as`, `as?`, and `is`:

```kotlin
if (fooInspektion.isInstance(someValue)) {
    val casted = fooInspektion.cast(someValue)
}

val safe = fooInspektion.safeCast(anotherValue)
```

These are particularly useful when you have an `Inspektion<*>` and need to perform type-safe operations in common code.

## Invoking methods

Inspekted functions can be invoked, and properties gotten and set.
See [Invoking Methods](invoking-methods.md) for details.

The class `Inspektion` object provides some utilities for invoking member functions,
such as `callFunction`, `getProperty`, and `setProperty`,
which are also available as the `invoke`, `get`, and `set` operators, respectively.
These methods only work when there is a single function or property for a given name - if that's not the case, you can still find the member using `functions` or `properties` and invoke it using its own invocation methods.

## Best Practices

* **Reuse Inspektion objects:** Since each `inspekt()` call adds to your binary size, it is best to call it once and store the result in a property or variable if you need to use it multiple times.
* **Prefer member-specific inspekt:** If you only need a single function or property, use `inspekt(Foo::bar)` instead of `inspekt(Foo::class).function("bar")` to minimize the amount of generated metadata.