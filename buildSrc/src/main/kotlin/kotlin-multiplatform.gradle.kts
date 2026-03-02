import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("kotlin-shared")
}

val onlyJvm = providers.systemProperty("inspekt.onlyJvm").orNull?.lowercase() == "true"

kotlin {
    jvm()
    js() {
        nodejs()
    }
    if (!onlyJvm) {
        js() {
            browser()
        }
        @OptIn(ExperimentalWasmDsl::class)
        wasmJs() {
            d8()
            nodejs()
            browser()
        }
        @OptIn(ExperimentalWasmDsl::class)
        wasmWasi() {
            nodejs()
        }

        // Apple
        macosX64()
        macosArm64()
        iosArm64()
        iosX64()
        iosSimulatorArm64()
        watchosArm32()
        watchosArm64()
        watchosX64()
        watchosSimulatorArm64()
        watchosDeviceArm64()
        tvosArm64()
        tvosX64()
        tvosSimulatorArm64()

        // Linux
        linuxX64()
        linuxArm64()

        // Windows
        mingwX64()

        // Android Native
        androidNativeArm32()
        androidNativeArm64()
        androidNativeX86()
        androidNativeX64()
    }

    applyDefaultHierarchyTemplate()
}

dependencies {
    commonTestImplementation(kotlin("test"))
}