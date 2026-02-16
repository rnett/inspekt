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
rootProject.name = "spekt"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

val projectVersion = providers.fileContents(layout.rootDirectory.file("version.txt")).asText.get().trim()

gradle.beforeProject {
    group = "dev.rnett.spekt"
    version = projectVersion
}

include(
    "spekt",
    "api",
    "compiler-plugin",
    "gradle-plugin"
)