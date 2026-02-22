[![Maven Central Version](https://img.shields.io/maven-central/v/dev.rnett.inspektion/inspektion?style=for-the-badge)](https://central.sonatype.com/artifact/dev.rnett.inspektion/inspektion)
[![Maven snapshots](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fcentral.sonatype.com%2Frepository%2Fmaven-snapshots%2Fdev%2Frnett%2Finspektion%2Finspektion%2Fmaven-metadata.xml&strategy=latestProperty&style=for-the-badge&label=SNAPSHOT&color=yellow)]()
[![Documentation](https://img.shields.io/badge/Documentation-2C3E50?style=for-the-badge&logo=googledocs&logoColor=D7DBDD)](https://inspektion.rnett.dev/latest/)
[![GitHub License](https://img.shields.io/github/license/rnett/inspektion?style=for-the-badge)](./LICENSE)

# Inspekt

100% multiplatform, 100% compile time reflection for Kotlin. Includes:

* Inspecting classes, functions, and properties
* Top-level declaration support
* Function invocation, including `suspend` functions
* `is` checking and casting
* JVM-like proxies
* Reflecting over dependencies

## Getting started

To use Inspekt, just apply the Gradle plugin:

```kotlin
plugins {
    id("dev.rnett.inspekt") version "<version>" // see the Maven Central badge above
}
```

and call `inspekt` from your code:

```kotlin
val fooClass = inspekt(Foo::class)

foo.function("bar").invoke {
    dispatchReceiver = Foo()
}
```

## Documentation

More detailed documentation is available at https://inspektion.rnett.dev/latest/.