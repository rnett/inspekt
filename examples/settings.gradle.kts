pluginManagement {
    val inspektVersion = providers.fileContents(layout.rootDirectory.file("../version.txt")).asText.get().trim()
    plugins {
        id("dev.rnett.inspekt") version inspektVersion
    }
    repositories {
        mavenCentral()
        maven("https://central.sonatype.com/repository/maven-snapshots/")
        gradlePluginPortal()
        mavenLocal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "inspekt-examples"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://central.sonatype.com/repository/maven-snapshots/")
        mavenLocal()
    }
    versionCatalogs {
        register("libs") { from(files("../gradle/libs.versions.toml")) }
    }
}

include("hello-world")
include("graalvm")
include("java-interop")
