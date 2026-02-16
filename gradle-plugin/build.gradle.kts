plugins {
    id("kotlin-jvm")
    alias(libs.plugins.kcp.dev.gradle)
    id("java-gradle-plugin")
}

compilerSupportPluginDevelopment {
    compilerPluginProjectPath = ":compiler-plugin"
}

gradlePlugin {
    plugins {
        create("SpektPlugin") {
            id = group.toString()
            displayName = "Spekt Gradle Plugin"
            description = "Spekt Gradle Plugin - a Kotlin Multiplatform logging facade"
            implementationClass = "dev.rnett.spekt.SpektPlugin"
        }
    }
}

//TODO opt into the compiler plugin annotation