# Proxies

Inspekt allows you to create proxies for interfaces at runtime.
This provides a similar mechanism to `java.lang.reflect.Proxy` on the JVM, but is completely multiplatform and created at compile-time.

## Creating a Proxy

To create a proxy for an interface, use the `proxy()` function and provide a `ProxyHandler`:

```kotlin
val myProxy = proxy(MyInterface::class) {
    // handler implementation
}
```

## The Proxy Handler

A `ProxyHandler` is a functional interface that receives all calls to the proxy's methods. Your implementation of `handle()` decides how to respond to the call.

```kotlin
val handler = ProxyHandler {
    // This block is called for every function call on the proxy
    println("Calling ${functionName} with arguments: $args")

    // Return the result of the call
    "Return value"
}
```

The handler is called with the `SuperCall` receiver, which gives it access to both the argument of the call, and the inspektion of the original called method. Some properties of note:

* `functionName: String` and `propertyName: String?` - the short names of the function, and the property if the function is an accessor
* `args: ArgumentList` - the arguments passed to the call, with convenient accessors for different parameters
* `isSuperCallable: Boolean` - whether the super function can be called
* `isSuspending: Boolean` - whether the call and function are suspending
* `callSuper()`/`callSuperSuspend()` - call the original implementation of the function with the same arguments, or with an arguments builder

### Suspend Support

If the interface has `suspend` functions, you can implement `handleSuspend()`:

```kotlin
val handler = object : ProxyHandler {
    override fun handle(): Any? { /* ... */
    }

    override suspend fun handleSuspend(): Any? {
        // Handle suspend calls here
        return someAsyncOperation()
    }
}
```

If not implemented, calling suspend methods will use `handle`.