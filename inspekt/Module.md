# Module inspekt

The Inspekt runtime library.

# Package dev.rnett.inspekt

Inspektion creation methods.

The values these methods return are created when the method is called, without any caching -
a call to this method is transformed into a [Inspektion](dev.rnett.inspekt.model.Inspektion) (or function or property model) constructor call by the compiler plugin.
All the arguments are calculated at compilation time.

Because of this, uses of this function can potentially add a significant amount of binary size.
This is based on the number of appearances of `inspekt()` in your code and how many members and parameters the argument has, not how many times it is invoked.

Calling this function on a declaration that is or contains functions that have a large number of default parameters can result in
inefficient invoke methods and significantly more binary size bloat.
A compiler warning will be shown in this case.

# Package dev.rnett.inspekt.model

Data models for Kotlin declarations.

# Package dev.rnett.inspekt.model.arguments

Builders and accessors for argument lists.

# Package dev.rnett.inspekt.model.name

Declaration names.

# Package dev.rnett.inspekt.proxy

Proxy creation methods.

Proxies work like the Java Proxy - they are anonymous objects that implement an interface using a "function handler" lambda for all method calls.
A call to a proxy creation method is transformed into an anonymous object instance by the compiler plugin.
All the arguments are calculated at compilation time.

Because of this, uses of this function can potentially add a significant amount of binary size.
This is based on the number of appearances of `proxy()`, etc. in your code and how many members and parameters the argument has, not how many times it is invoked.
If you find yourself repeatedly creating proxies for the same class, consider using [proxyFactory](dev.rnett.inspekt.proxy.proxyFactory), which has a constant binary overhead per factory invocation.

# Package dev.rnett.inspekt.exceptions

Exceptions and error markers.

# Package dev.rnett.inspekt.utils

Utilities and helpers.