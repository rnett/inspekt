---
name: compiler_testing
description: >-
  Audits, validates, and verifies Kotlin compiler plugin transformations and diagnostics. 
  Orchestrates testing using Golden File comparisons across .fir.txt and .ir.txt dumps.
  Automates test actualization and handles runtime verification with .box.txt phase tests.
  Use when validating plugin behavior, auditing test coverage, or reproducing compiler issues.
metadata:
  author: rnett
  version: "2.0"
---

# Compiler Testing

Provides authoritative guidance for executing and validating compiler plugin tests using the internal test harness.

## Constitution

- **Golden File Authority:** The textual dumps (.fir.txt and .ir.txt) represent the authoritative semantic and structural truth of the compiler.
- **Reproduction First:** ALWAYS reproduce bugs or features in a new `.kt` file within `testData/auto/` before modifying implementation code.
- **Phase-Ordered Validation:** ALWAYS align phases in sequence: `.kt` diagnostics -> `.fir.txt` (frontend) -> `.ir.txt` (backend) -> `.box.txt` (runtime).
- **Exhaustive Coverage:** Every new FIR diagnostic MUST have a test in `diagnostics/`, and every IR generation MUST have a `box/` test and an `.ir.txt` dump.
- **Language Level Compliance:** ALWAYS use `// LANGUAGE: +FeatureName` for tests targeting preview or experimental features (e.g., Context Parameters).

## Workflow

### 1. Research & Analysis

- **Locate Test Data:** Identify the target subdirectory in `compiler-plugin/src/testData/auto/`.
- **Audit Existing Coverage:** Use `find_files_by_glob` to check if similar features are already tested.
- **Determine Test Strategy:** Choose between diagnostic tests (expecting errors/warnings) and code-gen tests (expecting specific IR and correct execution).

### 2. Strategic Planning

- **Draft Source Case:** Formulate a minimal `.kt` file demonstrating the target behavior.
- **Identify Diagnostic Markers:** If testing FIR checkers, identify the expected diagnostic codes (e.g., `<!DIAGNOSTIC!>`).
- **Define Success Criteria:** Determine the expected `box()` result and the critical sections of the IR tree to verify.

### 3. Execution (Act)

- **Step 1: Run Test Runner:** Execute the relevant test class via Gradle.
  ```bash
  ./gradlew :compiler-plugin:test --tests "dev.rnett.inspekt.auto.*"
  ```
- **Step 2: Align Source Diagnostics:** Resolve diffs in the `.kt` file by marking locations with diagnostic markers.
- **Step 3: Verify Frontend (FIR):** Audit the `.fir.txt` dump. Check if synthetic declarations exist and if types are correctly resolved.
- **Step 4: Verify Backend (IR):** Audit the `.ir.txt` dump. Ensure the generated IR is structurally correct and matches the designed transformation.
- **Step 5: Run Box Verification:** Confirm the compiled code executes and returns `"OK"`.

### 4. Validation

- **Update Actuals:** Once behavior is verified manually, use `-Pdev.rnett.kcp.testing.updateActuals=true` to update the golden files.
- **Debug Regression:** If a dump changes unexpectedly, use `-Xphases-to-dump` or `FirElement.render()` to isolate the cause.
- **Sanitize Environment:** Run `clearDumps` to ensure a clean baseline for the next test cycle.

## Examples

### Identifying a Regression in FIR Dump

**Scenario:** A change to `InspektChecker` causes a diff in `AnnotatedClass.fir.txt`.

1. **Reasoning:** I've added a new validation for @Inspekt annotations. I need to verify that the FIR tree still resolves the annotation correctly.
2. **Analysis:** Review the diff. If the annotation is missing or its type is unresolved (`ERROR_TYPE`), the checker implementation is likely incorrect.
3. **Debugging:** Use `FirElement.render()` on the target class during test execution to see its internal state.
4. **Action:** Fix the checker until the `.fir.txt` dump matches the intended behavior.
5. **Finalization:** Update the golden file and commit the new dump.

## Verification Checkpoints

- [ ] Has a reproduction test case been added to the correct `testData` category?
- [ ] Are all frontend diagnostics correctly marked in the `.kt` source?
- [ ] Do the `.fir.txt` and `.ir.txt` dumps represent the intended semantic truth?
- [ ] Does the code pass the `box()` execution test?
- [ ] Have all stale dumps been cleared via `clearDumps`?

## Resources

- [Compiler Research]({baseDir}/references/compiler_plugin_research.md)
- [Test Runner Reference]({baseDir}/references/test_runner.md)
