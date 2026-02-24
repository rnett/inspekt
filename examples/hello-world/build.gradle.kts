plugins {
    kotlin("multiplatform")
    id("dev.rnett.inspekt")
}

kotlin {
    jvm {
        mainRun {
            mainClass.set("dev.rnett.inspekt.example.MainKt")
        }
    }

    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
    wasmJs {
        nodejs()
        binaries.executable()
    }

    js(IR) {
        nodejs()
        binaries.executable()
    }

    listOf(
        linuxX64(),
        linuxArm64(),
        macosX64(),
        macosArm64(),
        mingwX64()
    ).forEach { target ->
        target.binaries {
            executable {
                entryPoint = "dev.rnett.inspekt.example.main"
            }
        }
    }

    applyDefaultHierarchyTemplate()

    sourceSets {
        commonMain {
            dependencies {
                // Inspekt dependency is added automatically by the plugin
            }
        }
    }
}
