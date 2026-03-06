# Kotlin Compiler API Reference (inspekt)

This reference provides key details about the compiler APIs and project-specific utilities.

## Core Compiler APIs (K2)

### FIR (Frontend IR)

- **FirSession**: Central entry point for all frontend operations.
- **FirSymbol**: Represents a declaration in the frontend.
- **FirChecker**: Interface for implementing diagnostics and validation.

### IR (Intermediate Representation)

- **IrPluginContext**: Primary entry point for IR transformations.
- **IrBuilderWithScope**: Base class for generating IR. Use `withBuilder` to initialize.
- **IrClass**, **IrFunction**, **IrProperty**: Core IR declaration types.

## Project-Specific Utilities

### Spekt Generation

- **SpektGenerator**: Handles the generation of the reflection model (`Spekt`).
- **GenerationContext**: State object passed around during generation.
- **createParamObject**: Helper for generating metadata for a parameter.

### Proxy Generation

- **ProxyGenerator**: Handles the generation of interface proxy implementations.
- **generateProxy**: Entry point for proxy creation.

### KCP Development Utils

- **irCall**: Simplified IR function call.
- **irString**: Creates an `IrConst<String>`.
- **irTypeOf**: Generates code to obtain a `KType` at runtime.
- **eraseTypeParameters**: Erases type parameters to avoid issues with generics in reflection metadata.
