### Handling Dynamic Bitmasks for Default Arguments

Since the built-in Kotlin default-argument mechanism calculates bitmasks strictly at **compile-time**, it cannot be used directly for dynamic (runtime) masks. The `DefaultParameterInjector` calculates masks by checking which arguments are
`null` (or missing) in the `IrCall` and then emits `IrConst(Int)` values for the masks.

If you need to decide at runtime which parameters should take their default values, here are the recommended alternative approaches:

---

### 1. The "Body-Side Defaulting" Pattern (Recommended)

This is the most robust approach for IR plugins. Instead of relying on the `$default` bridge, you transform the function and its calls to handle defaults internally.

**Transformation:**

1. **Change the signature:** Make the parameters nullable (if they aren't already).
2. **Move logic to body:** At the start of the function body, check if the parameter is `null`. If it is, assign the default value.
3. **Call site:** Pass `null` at runtime when you want the default value to be used.

**Example IR change:**

```kotlin
// Original
fun foo(x: Int = 1) { ... }

// Transformed for dynamic defaults
fun foo(x: Int?) {
    val x_actual = x ?: 1
    ...
}
```

**Pros:** Backend-agnostic, handles dynamic logic naturally, avoids `$default` signature complexity.
**Cons:** Requires changing the function signature (breaking binary compatibility if that matters).

---

### 2. The "Wrapper Dispatch" Pattern

If you cannot change the target function's signature, you can generate a wrapper that uses a `when` or `if` tree to call the function with different static patterns.

**Example Logic:**

```kotlin
fun dynamicCaller(mask: Int, arg0: Int, arg1: Int) {
    when (mask) {
        0 -> foo(arg0, arg1)      // Both provided
        1 -> foo(x = arg0)        // arg1 defaulted
        2 -> foo(y = arg1)        // arg0 defaulted
        3 -> foo()                // Both defaulted
    }
}
```

**Pros:** Works with existing functions without changing them.
**Cons:** Exponential complexity ($2^n$ branches) as the number of defaultable parameters increases.

---

### 3. Manual Bridge Generation (The "Compose" way)

If you want to maintain a single bridge function similar to `$default` but with a dynamic mask, you must synthesize your own "bridge" function in your plugin.

**Steps:**

1. Define a new function (e.g., `foo$plugin_default`) that accepts all original parameters plus one or more `Int` mask parameters.
2. In the body of this bridge, use the mask bits to decide whether to call the original function with the passed value or the default value.
3. Have your plugin rewrite calls to target this new bridge.

This effectively replicates what the Kotlin compiler does but gives you control over the mask parameter so you can pass a non-constant `IrExpression`.

---

### Why calling `$default` directly is not viable

As established earlier, calling the standard `$default` bridges from an `IrGenerationExtension` is problematic because:

- **Symbol Availability:** The bridges are created by the backend *after* your plugin runs. You would have to guess the symbol and signature.
- **Signature Variance:** The signature changes depending on the backend (JVM adds `handler`, constructors add `DefaultConstructorMarker`, etc.).
- **Mask Stability:** The order of mask parameters and which bits correspond to which parameters is an internal implementation detail of `MaskedDefaultArgumentFunctionFactory` and can change between Kotlin versions.

### Summary of what "Compose" does

The Compose compiler plugin avoids Kotlin’s default argument mechanism entirely for `@Composable` functions. It:

1. Adds its own `$changed` and `$default` bitmask parameters to the function signature.
2. Generates code at the start of the function body that checks the `$default` mask and executes the default expression if the bit is set.
3. Calculates these masks dynamically at call sites based on state changes.

**Recommendation:** Follow the **Body-Side Defaulting** (Option 1) if you can change the signature, or the **Manual Bridge** (Option 3) if you need a high-performance, stable bridge-like mechanism.
