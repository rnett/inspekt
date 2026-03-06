---
name: compiler_plugin_dev
description: >-
  Provides architectural guidance and patterns for developing the inspekt Kotlin compiler plugin. 
  Guides modifications to SpektGenerator (IR), ProxyGenerator (IR), and InspektChecker (FIR) 
  to implement reflection metadata generation, interface proxies, or frontend diagnostics. 
  Use when implementing new reflection features or fixing compiler plugin bugs.
metadata:
  author: rnett
  version: "1.3"
---

# Compiler Plugin Development

This skill provides architectural guidance and patterns for modifying the `inspekt` compiler plugin.

## Constitution

- **K2/FIR Mandatory:** ALWAYS target the latest K2/FIR compiler APIs. NEVER use K1/Legacy compiler APIs.
- **Symbol Integrity:** ALWAYS use the generated `Symbols` object to access runtime declarations. NEVER manually look up runtime class IDs if they are available in `Symbols`.
- **Context Separation:** ALWAYS maintain strict separation between FIR (frontend) and IR (backend) logic.
- **Runtime-First Generation:** ALWAYS prefer calling runtime helpers over generating complex IR code directly.
- **Context Parameters:** Context parameters are ONLY supported on functions and properties. NEVER attempt to apply them to classes or interfaces.
- **Visibility Compliance:** ALWAYS check visibility using `visibility.isPublicAPI` when generating calls to ensure binary compatibility.

## Workflow

### 1. Research & Analysis

- **Identify Target:** Locate the relevant generator or checker in `compiler-plugin/src/main/kotlin/` (e.g., `SpektGenerator.kt`, `ProxyGenerator.kt`).
- **Determine API:** Identify whether the change requires FIR (frontend diagnostics/validation) or IR (backend code generation).
- **Search Precedents:** Utilize `grep_search` to find similar patterns in the codebase (e.g., "irCall", "withBuilder", "eraseTypeParameters").

### 2. Strategic Planning

- **Transformation Mapping:** Map the desired source code to the target FIR/IR structure.
- **Symbol Resolution:** Identify all runtime symbols required for the transformation.
- **Test Plan:** Formulate a reproduction test case in `compiler-plugin/src/testData/auto/`.

### 3. Implementation (Act)

- **Initialize Builder:** Use `withBuilder(context)` to start IR generation.
- **Generate Code:** Implement the transformation using `kcp-development` utilities (e.g., `irCall`, `irString`).
- **Handle Generics:** Use `eraseTypeParameters()` when generating reflection metadata for generic types.

### 4. Phase-Based Validation

- **Phase 1 (Tests):** Run `./gradlew :compiler-plugin:test` and verify that the reproduction test case fails as expected.
- **Phase 2 (Actuals):** Apply the fix and verify that the generated FIR/IR actuals match the new design.
- **Phase 3 (Box):** Confirm that the `box()` tests pass, ensuring runtime correctness.

## Examples

### Adding a new field to reflection metadata

**Scenario:** Add a `isContext` boolean to parameter metadata.

1. **Reasoning:** I need to update `SpektGenerator.createParamObject` to include the `isContext` information.
2. **Action (Runtime):** Update `Parameter` class in `inspekt` module (outside this skill's scope).
3. **Action (Compiler):**
   ```kotlin
   // Inside SpektGenerator.createParamObject
   arguments[isContextKind] = irBoolean(param.kind == IrParameterKind.Context)
   ```
4. **Validation:** Run tests and check `.ir.txt` to see the new `irBoolean` call in the metadata object.

## Verification Checkpoints

- [ ] Does the change use K2/FIR APIs exclusively?
- [ ] Are all runtime symbols retrieved via the `Symbols` object?
- [ ] Has a reproduction test case been added to `compiler-plugin/src/testData/auto/`?
- [ ] Do all FIR, IR, and box tests pass?
- [ ] Has `eraseTypeParameters()` been used where reflection metadata for generics is generated?

## Resources

- [Compiler API Reference]({baseDir}/references/compiler_api.md)
