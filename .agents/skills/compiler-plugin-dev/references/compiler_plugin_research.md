# K2 Compiler Architecture: Key Takeaways

## 1. FIR (Frontend Intermediate Representation)

The K2 frontend uses a phased semantic tree. Extensions must respect the temporal availability of information.

### FIR Resolve Phases

| Phase            | Information Available                                |
|:-----------------|:-----------------------------------------------------|
| `SUPER_TYPES`    | Class hierarchy and interfaces.                      |
| `TYPES`          | Explicit declaration headers (params, return types). |
| `STATUS`         | Visibility, modality, and modifiers.                 |
| `IMPLICIT_TYPES` | Inferred types (from initializers/bodies).           |
| `BODY_RESOLVE`   | Full expression resolution and local declarations.   |
| `CHECKERS`       | Final validation and diagnostic reporting.           |

### Extension Protocols

- **Pull-Based Generation:** Plugins do not "push" declarations. The compiler "pulls" them:
    1. **Discovery:** `getCallableNamesForClass` / `getNestedClassifiersNames`.
    2. **Creation:** `generateFunctions` / `generateClassLikeDeclaration`.
    3. **Resolution:** Synthetic nodes must be marked as `FirResolvePhase.BODY_RESOLVE`.
- **Symbols:** `FirDeclaration` nodes are linked to stable `FirSymbol`s. Always use symbols for cross-references.
- **IDE Synergy:** FIR extensions run in the IDE. They MUST be resilient to partial/broken code to prevent "red code" or IDE crashes.

## 2. IR (Backend Intermediate Representation)

Lower-level, mutable tree used for code generation and optimizations across targets (JVM, JS, Native, Wasm).

### IR Transformation

- **Lowering:** High-level constructs (e.g., `for` loops) are "lowered" into lower-level ones (e.g., `while`).
- **Validation:** K2 enforces strict structural integrity. Use `-Xverify-ir` to catch arg-slot mismatches or type errors early.
- **Builders:** Use `IrBuilderWithScope` and `IrFactory` for surgical tree modifications.

## 3. Testing & Validation

The internal harness uses "Golden File" comparisons to verify compiler state.

### Testing Protocol

- **Diagnostics:** Verify FIR checkers report correct markers in `.kt` files.
- **Dumps:** Audit `.fir.txt` (semantic truth) and `.ir.txt` (structural truth).
- **Box Tests:** Final runtime verification; code must execute and return `"OK"`.
- **Debugging:** Use `-Xphases-to-dump` to see IR state before/after specific lowerings or plugin transformations.
