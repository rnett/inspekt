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
