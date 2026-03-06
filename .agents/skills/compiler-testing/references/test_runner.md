# KCP Development Test Runner Reference

The `inspekt` project uses the `kcp-development` testing library to automate compiler plugin tests.

## Test Directives

Directives are special comments at the top of `.kt` files in `testData/auto`.

- `// LANGUAGE: +ContextParameters`: Enables the context parameters feature.
- `// COMPILER_ARGUMENTS: <args>`: Passes additional arguments to the compiler.
- `// WITH_STDLIB`: Adds the Kotlin standard library to the classpath.
- `// FULL_JDK`: Adds the full JDK to the classpath.

## Test Levels

The test runner executes different "levels" of tests based on the directory structure:

- `diagnostics`: Runs frontend analysis and checks for expected diagnostic markers.
- `fir`: Dumps the FIR tree and compares it to `.fir.txt`.
- `ir`: Dumps the IR tree and compares it to `.ir.txt` (or `.fir.ir.txt`).
- `box`: Compiles the code and executes the `box()` function, which must return `"OK"`.

## Troubleshooting

- **Missing actuals:** If expected files (`.txt`) are missing, the test runner will fail the test and generate the actual output. Review the actual output in the test report or the console.
- **Dumping actuals:** Use `./gradlew :compiler-plugin:clearDumps` to clear previous test output before rerunning to ensure fresh results.
