---
name: compiler_plugin_dev
description: >-
  Designs, models, and implements Kotlin compiler extensions using K2/FIR and IR. 
  Guides modifications to SpektGenerator (IR), ProxyGenerator (IR), and InspektChecker (FIR).
  Handles synthetic declaration generation, FIR status/supertype transformations, and IR backend lowerings.
  Use when implementing reflection features, fixing plugin bugs, or engineering compiler diagnostics.
metadata:
  author: rnett
  version: "2.0"
---

# Compiler Plugin Development

Provides authoritative guidance for developing the `inspekt` compiler plugin within the K2 architecture.

## Constitution

- **K2/FIR Mandatory:** ALWAYS target the latest K2/FIR compiler APIs. NEVER use K1/Legacy compiler APIs.
- **Symbol Integrity:** ALWAYS use the generated `Symbols` object to access runtime declarations.
- **FIR Phase Awareness:** ALWAYS verify declaration availability against the current FIR resolve phase (e.g., `SUPER_TYPES` for hierarchy, `TYPES` for headers).
- **Pull-Based Generation:** ALWAYS implement `FirDeclarationGenerationExtension` using the pull-based protocol (Name Discovery -> Symbol Creation -> Resolution Completion).
- **Context Separation:** ALWAYS maintain strict separation between FIR (frontend) and IR (backend) logic.
- **IR Stability:** ALWAYS use `-Xverify-ir` during development to catch structural inconsistencies in backend transformations.
- **IDE Stability:** FIR extensions MUST handle partial or broken code gracefully to prevent IDE crashes in K2 mode.
- **Visibility Compliance:** ALWAYS check visibility using `visibility.isPublicAPI` for binary compatibility.

## Workflow

### 1. Research & Analysis

- **Identify Extension Point:** Choose between `FirExtensionRegistrar` (frontend) and `IrGenerationExtension` (backend).
- **Phase Mapping:** Consult the FIR lifecycle to determine the earliest phase where required semantic information is available.
- **Symbol Audit:** Identify required runtime symbols in `Symbols.kt` or `Names.kt`.

### 2. Strategic Planning

- **Synthetic Protocol:** For new declarations, plan the 3-step generation protocol (getNames -> generateSymbols -> resolve).
- **Transformation Strategy:** Map source constructs to IR nodes. Plan for actualization in KMP if targeting common source sets.
- **Test Design:** Define expected `.fir.txt` and `.ir.txt` dumps based on the planned transformation.

### 3. Implementation (Act)

- **Frontend (FIR):**
    - Implement checkers using `FirAdditionalCheckersExtension`.
    - Implement generation using `FirDeclarationGenerationExtension`.
    - Ensure synthetic declarations are marked as `FirResolvePhase.BODY_RESOLVE` when returned.
- **Backend (IR):**
    - Initialize `IrBuilderWithScope` via `withBuilder(context)`.
    - Utilize `kcp-development` utilities (`irCall`, `irString`, `irArrayOf`) to minimize boilerplate.
    - Use `eraseTypeParameters()` for reflection metadata generation.

### 4. Validation

- **Diagnostic Check:** Verify FIR checkers report expected errors in the IDE and during build.
- **Dump Alignment:** Run tests and align `.fir.txt` and `.ir.txt` actuals with the design.
- **Binary Compatibility:** Audit visibility and origin (`IrDeclarationOrigin`) of generated nodes.

## Examples

### Implementing Synthetic Declaration Generation

**Scenario:** Generate a nested `Spekt` object for any class annotated with `@Inspekt`.

1. **Discovery:** `getNestedClassifiersNames` returns `Name.identifier("Spekt")` if the class has the annotation.
2. **Creation:** `generateClassLikeDeclaration` builds a `FirRegularClass` symbol.
3. **Resolution:** Ensure the class modality is `FINAL` and visibility is `PUBLIC`.
4. **IR Backend:** Implement the `Spekt` object's members in `SpektGenerator` using the `IrPluginContext`.

## Verification Checkpoints

- [ ] Does the change respect the FIR phased resolution timeline?
- [ ] Are synthetic declarations implemented using the pull-based provider API?
- [ ] Has `-Xverify-ir` been used to validate backend structural integrity?
- [ ] Is the implementation stable against partial/broken code for K2 IDE mode?
- [ ] Have KMP actualization requirements been addressed for common declarations?

## Resources

- [Compiler Research]({baseDir}/references/compiler_plugin_research.md)
- [Compiler API Reference]({baseDir}/references/compiler_api.md)
