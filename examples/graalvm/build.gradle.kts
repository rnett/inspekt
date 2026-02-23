plugins {
    kotlin("jvm")
    alias(libs.plugins.graalvm.native)
    id("dev.rnett.inspekt")
    application
}

application {
    mainClass.set("dev.rnett.inspekt.example.Main")
}

java {
    toolchain { version = JavaLanguageVersion.of(21) }
}

kotlin {
    jvmToolchain(21)
}

graalvmNative {
    toolchainDetection = false
    useArgFile = false
    binaries {
        named("main") {
            javaLauncher = javaToolchains.launcherFor {
                vendor = JvmVendorSpec.BELLSOFT
                languageVersion = JavaLanguageVersion.of(21)
                nativeImageCapable = true
            }
            fallback = false
            debug = true
            verbose = true
            mainClass.set("dev.rnett.inspekt.example.Main")
            richOutput = true
            resources.autodetect()
        }
    }
}