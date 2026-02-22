import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.HasConfigurableKotlinCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.abi.AbiValidationExtension
import org.jetbrains.kotlin.gradle.dsl.abi.AbiValidationMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.abi.ExperimentalAbiValidation
import org.jetbrains.kotlin.powerassert.gradle.PowerAssertGradleExtension

plugins {
    id("org.jetbrains.kotlin.plugin.power-assert")
}

val kotlin = project.extensions.findByName("kotlin") as KotlinBaseExtension
val onlyJvm = providers.systemProperty("inspekt.onlyJvm").orNull?.lowercase() == "true"

kotlin.apply {
    this as HasConfigurableKotlinCompilerOptions<out KotlinCommonCompilerOptions>

    if (!pluginManager.hasPlugin("internal")) {
        explicitApi()
    }
    jvmToolchain(17)
    sourceSets.all {
        languageSettings {
            optIn("kotlin.RequiresOptIn")
            optIn("kotlin.contracts.ExperimentalContracts")
        }
    }

    compilerOptions {
        freeCompilerArgs.addAll(
            "-Xannotation-default-target=param-property",
            "-Xcontext-parameters",
            "-Xconsistent-data-class-copy-visibility",
            "-Xexpect-actual-classes"
        )
    }
}

if (!pluginManager.hasPlugin("internal")) {
    if (kotlin is KotlinMultiplatformExtension) {
        @OptIn(ExperimentalAbiValidation::class)
        kotlin.the<AbiValidationMultiplatformExtension>().apply {
            enabled = true
            klib {
                enabled = !onlyJvm
                keepUnsupportedTargets = true
            }
        }
    } else if (kotlin is KotlinJvmProjectExtension) {
        @OptIn(ExperimentalAbiValidation::class)
        kotlin.the<AbiValidationExtension>().apply { enabled = true }
    } else {
        error("Unsupported kotlin type: $kotlin")
    }

    tasks.named("check").configure {
        dependsOn("checkLegacyAbi")
    }
}

plugins.withId("org.jetbrains.kotlin.plugin.power-assert") {
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    extensions.configure<PowerAssertGradleExtension>("powerAssert") {
        functions = listOf(
            "kotlin.assert",
            // kotlin.test
            "kotlin.test.assertTrue",
            "kotlin.test.assertEquals",
            "kotlin.test.assertNotEquals",
            "kotlin.test.assertNull",
            "kotlin.test.assertNotNull",
            "kotlin.test.assertFails",
            "kotlin.test.assertFailsWith",
            "kotlin.test.assertContains",
            "kotlin.test.assertContentEquals",
            "kotlin.test.assertIs",
            "kotlin.test.assertIsNot",
            "kotlin.test.assertSame",
            "kotlin.test.assertNotSame",
        )
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    systemProperty("junit.jupiter.execution.parallel.enabled", "true")
    systemProperty("junit.jupiter.execution.parallel.mode.classes.default", "concurrent")
}
