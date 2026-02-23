# Inspekt Examples

This directory contains examples of how to use Inspekt.

## Examples

- [hello-world](./hello-world): A simple project using Inspekt on multiple platforms.
- [java-interop](./java-interop): A JVM project showcasing Inspekt's interop with Java classes.
- [graalvm](./graalvm): A project demonstrating Inspekt's compatibility with GraalVM Native Image.

## Running the examples

To run the examples, use the provided Gradle wrapper:

### Hello World (JVM)

```bash
./gradlew :hello-world:run
```

### Multiplatform

```bash
./gradlew :multiplatform:jvmRun
```
