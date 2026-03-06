---
name: compiler_testing
description: >-
  Manages, executes, and validates Kotlin compiler plugin tests within the inspekt project. 
  Automates testing across .fir.txt, .ir.txt, and .box.txt phases. 
  Use when creating new test cases in testData/auto, debugging compiler failures, or generating test actuals.
metadata:
  author: rnett
  version: "1.4"
---

# Compiler Testing

This skill provides the workflow and standards for testing the `inspekt` compiler plugin.

## Constitution

- **Reproduction Priority:** ALWAYS create a reproduction test case in `compiler-plugin/src/testData/auto/` before applying a fix.
- **Phase-Ordered Alignment:** ALWAYS resolve phases in order: `.kt` diagnostics -> `.fir.txt` -> `.ir.txt` -> `.box.txt`. NEVER move to the next phase until the current one is perfectly aligned.
- **Exhaustive Verification:** ALWAYS ensure FIR, IR, and box levels are all verified for new features.
- **Source Integrity:** ALWAYS maintain consistent indentation and formatting in `.kt` test files.
- **Language Feature Resolution:** When adding tests for new features (e.g. Context Parameters), ALWAYS ensure the correct `// LANGUAGE: +FeatureName` directive is present.

## Workflow

### 1. Research & Analysis

- **Identify Test Area:** Locate the relevant test directory in `compiler-plugin/src/testData/auto/`.
- **Determine Level:** Decide if the test should target `diagnostics`, `fir`, `ir`, or `box`.
- **Directive Lookup:** Search for valid `// LANGUAGE` or `// COMPILER_ARGUMENTS` directives if specialized behavior is required.

### 2. Test Materialization (Plan)

- **Draft Test Code:** Create a `.kt` file that demonstrates the target behavior.
- **Define Expected Dumps:** Plan what the FIR and IR dumps should look like for the new feature.

### 3. Execution & Phase-Based Alignment (Act)

- **Step 1: Run Baseline:** Execute the test and observe the failure.
  ```bash
  ./gradlew :compiler-plugin:test --tests "dev.rnett.inspekt.auto.*"
  ```
- **Step 2: Align Source Diagnostics (.kt):** Resolve any diffs in the `.kt` file by adding expected diagnostic markers (e.g., `<!DIAGNOSTIC!>code<!>`).
- **Step 3: Align Frontend (FIR):** Verify `.fir.txt`. If the dump is incorrect, adjust the `InspektChecker` or update the expected dump.
- **Step 4: Align Backend (IR):** Verify `.ir.txt`. If the transformation is incorrect, adjust the `SpektGenerator` or `ProxyGenerator`.
- **Step 5: Verify Execution (Box):** Confirm the code runs and returns `"OK"`.

### 4. Finalization (Validate)

- **Auto-Update Actuals:** Once behavior is confirmed, use `-Pdev.rnett.kcp.testing.updateActuals=true` to update all expected files.
- **Sanitize Environment:** Run `./gradlew :compiler-plugin:clearDumps` to ensure no stale data remains.

## Examples

### Aligning an IR dump after a generator change

**Scenario:** A change to `SpektGenerator` causes a diff in `MyTest.ir.txt`.

1. **Reasoning:** I've updated the generator to include a new metadata field. I need to verify that the IR dump reflects this change correctly.
2. **Action:** Run the test.
3. **Internal Review:** Compare the `actual` IR dump with the `expected`. Confirm the new `irCall` or `irGet` is in the correct position.
4. **Execution:** If correct, run the Gradle command with `updateActuals=true`.
5. **Validation:** Rerun the test normally to ensure it passes.

## Verification Checkpoints

- [ ] Does the test correctly reproduce the target behavior or bug?
- [ ] Have all `.kt` diagnostics been correctly marked?
- [ ] Is the `.fir.txt` dump consistent with the expected frontend behavior?
- [ ] Is the `.ir.txt` dump consistent with the expected backend transformation?
- [ ] Does the `box()` test return `"OK"`?

## Resources

- [Test Runner Reference]({baseDir}/references/test_runner.md)
