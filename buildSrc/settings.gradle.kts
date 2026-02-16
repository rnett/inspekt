dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
    versionCatalogs {
        register("libs") { from(files("../gradle/libs.versions.toml")) }
    }
}