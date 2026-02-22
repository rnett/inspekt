plugins {
    id("kotlin-jvm")
    alias(libs.plugins.kcp.dev.gradle)
    id("java-gradle-plugin")
    id("kotlin-publishing")
}

compilerSupportPluginDevelopment {
    compilerPluginProjectPath = ":compiler-plugin"
}

gradlePlugin {
    plugins {
        create("InspektPlugin") {
            id = group.toString()
            displayName = "Inspekt Gradle Plugin"
            description = "Inspekt Gradle Plugin - a Kotlin Multiplatform logging facade"
            implementationClass = "dev.rnett.inspekt.InspektPlugin"
        }
    }
}

buildConfig {
    buildConfigField("COMPILER_PLUGIN_INTRINSIC_ANNOTATION", "dev.rnett.inspekt.exceptions.InspektCompilerPluginIntrinsic")
    buildConfigField("LIBRARY_ARTIFACT_ID", "inspekt")
}