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
    binaries {
        named("main") {
            javaLauncher = javaToolchains.launcherFor {
                languageVersion = JavaLanguageVersion.of(21)
                nativeImageCapable = true
            }
            mainClass.set("dev.rnett.inspekt.example.Main")
        }
    }
}
