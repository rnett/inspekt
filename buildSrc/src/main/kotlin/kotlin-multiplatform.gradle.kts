import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("kotlin-shared")
}

val onlyJvm = providers.systemProperty("inspekt.onlyJvm").orNull?.lowercase() == "true"

kotlin {
    jvm()
    if (!onlyJvm) {
        js() {
            browser()
            nodejs()
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
        iosArm64()
        iosX64()
        mingwX64()
        macosX64()
        macosArm64()
        linuxX64()
        linuxArm64()
    }
}

dependencies {
    commonTestImplementation(kotlin("test"))
}