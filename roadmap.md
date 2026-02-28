## TODO

- The end

## Once FIR plugins are supported in the IDE

- `@Inspekt` to generate an `inspekt(): Inspektion<Self>` **instance** method and add an `Inspekted` interface. This is to avoid the need to pass around `this to inspekt(Self::class)` pairs
- Similar thing for proxying? Then an RPC server, for example, could take `T : Inspekted, Proxyable`

## Maybe

- Code reflection (i.e. bodies)? Can't access bodies