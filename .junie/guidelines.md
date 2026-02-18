# Project Guidelines

This is `spekt`, a Gradle project for a Kotlin Multiplatform compile time reflection library.

Use the Gradle MCP to interact with Gradle whenever possible.

Don't open files in the editor unless your intent is for me to look at them, and even then, ask first.
You can't read them from the editor, you need to read the file using something like IntelliJ's `get_file_text_by_path` MCP tool.

## Project structure notes

* The `test` task should be used to run tests for non-multiplatform modules.

## Code style notes

* Always name tests using descriptive names in English, using backticks.
* Always put dependencies in the version catalog.
* Use only the kotlin.test assertions configured with power-assert. Power-assert makes it unnecessary to use more complex assertions. Generally prefer to just use `kotlin.assert`.
* Do not under any circumstances use reflection hacks for tests.
* Always use `runTest` for suspending tests, not `runBlocking`.
* Try to minimize how many tokens you use.
* When writing KDocs, always use the short-name for links to classes, methods, etc. and import the target.
* When writing KDocs, always use multi-line KDocs.
* For pure common code, default to adding unit tests in `jvmTest`. Ask if unsure.
* Avoid using trivial helpers for tests just to make things more concise. If you're using a helper, there should be logic in it, or a decent amount of otherwise-duplicated code.

## Compiler Testing Guidelines

The project uses `kcp-development` for compiler plugin testing. Tests are located in `compiler-plugin/src/testData`.

### Test Structure

- **Test data for generation**: Located in `compiler-plugin/src/testData/auto`. The directory structure determines the test level and name.
- **Generated tests**: Located in `compiler-plugin/src/test-gen`, these tests are generated based on the test data.
- **Generation and test environment configuration**: Located in `compiler-plugin/src/testFixtures`

### Test Levels

- **FIR**: Frontend tests. Verifies that FIR extensions (like `SpektMethodGenerator`) correctly add declarations. Expected output in `.fir.txt`.
- **IR**: Backend tests. Verifies IR transformations. Expected output in `.fir.ir.txt` or `.ir.txt`.
- **Box**: Execution tests. Compiles the code and runs the `box(): String` function. Must return `"OK"` to pass.
- **Compile**: Verifies that the code compiles successfully through FIR and IR.
- **Diagnostics**: Verifies that no errors or expected warnings are present.

### Adding a Test Case

1. Create a `.kt` file in the appropriate directory under `compiler-plugin/src/testData/auto`.
   - `auto/spekt_method/fir`: For FIR-only tests.
   - `auto/implementation/box`: For execution tests.
   - `auto/implementation/compile`: For compilation-only tests.
   - etc
2. For **Box tests**, ensure there is a `fun box(): String` that returns `"OK"`, or returns a string and has a `<Test>.box.txt` file exists (which will be used as the expected value).
   - kotlin.test assertions may be used in these tests
3. Run the tests using `./gradlew :compiler-plugin:test`.
4. If it's a new test, it will fail and generate actual output files. Review them and ensure that they are correct.
5. If changes were made to the produced IR or FIR of existing tests, they will fail until the files are updated to match. The easiest way to do this is to delete the files (or rename them) and then re-run the tests.

### Guidelines for Test Data

- Keep test cases focused. One feature/case per file.
- Use `@Inspekt` to trigger the plugin.
- In `box()` tests, use `kotlin.test` assertions (e.g., `assertEquals`).
- The generated `*.txt` files are essentially snapshot tests for the compiler FIR and IR.
