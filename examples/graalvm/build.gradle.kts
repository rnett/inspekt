plugins {
    kotlin("jvm")
    alias(libs.plugins.graalvm.native)
    id("dev.rnett.inspekt")
    application
}

application {
    mainClass.set("dev.rnett.inspekt.example.Main")
}

graalvmNative {
    binaries {
        named("main") {
            javaLauncher = javaToolchains.launcherFor {
                languageVersion = JavaLanguageVersion.of(21)
                nativeImageCapable = true
            }
            imageName.set("inspekt-graalvm")
            mainClass.set("dev.rnett.inspekt.example.Main")
        }
    }
}
