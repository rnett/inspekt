pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://central.sonatype.com/repository/maven-snapshots/")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
rootProject.name = "inspekt-parent"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://central.sonatype.com/repository/maven-snapshots/")
    }
}

val projectVersion = providers.fileContents(layout.rootDirectory.file("version.txt")).asText.get().trim()

gradle.beforeProject {
    group = "dev.rnett.inspekt"
    version = projectVersion
}

include(
    "inspekt",
    "compiler-plugin",
    "gradle-plugin",
    "test-helpers"
)