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

    linuxX64() {
        binaries {
            executable()
        }
    }
    linuxArm64() {
        binaries {
            executable()
        }
    }

    mingwX64() {
        binaries {
            executable()
        }
    }

    macosX64() {
        binaries {
            executable()
        }
    }
    macosArm64() {
        binaries {
            executable()
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
