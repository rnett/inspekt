import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("kotlin-shared")
}

kotlin {
    jvm()
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

dependencies {
    commonTestImplementation(kotlin("test"))
}