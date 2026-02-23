import org.jetbrains.kotlin.gradle.targets.js.npm.tasks.KotlinNpmInstallTask

plugins {
    id("dokka")
}

tasks.register("updateLegacyAbi") {
    dependsOn(project.subprojects.map { it.tasks.named("updateLegacyAbi") })
}

tasks.register("checkLegacyAbi") {
    dependsOn(project.subprojects.map { it.tasks.named("checkLegacyAbi") })
}

tasks.register("checkAll") {
    group = "verification"
    dependsOn(project.subprojects.map { it.tasks.named("check") })
}

afterEvaluate {
    tasks.register("publishAllToMavenCentral") {
        group = "publishing"
        dependsOn(project.subprojects.flatMap { it.tasks.named({ it == "publishAllPublicationsToMavenCentralRepository" }) })
    }
    tasks.register("publishAllToMavenLocal") {
        group = "publishing"
        dependsOn(project.subprojects.flatMap { it.tasks.named({ it == "publishToMavenLocal" }) })
    }
}

dependencies {
    dokka(project(":inspekt"))
    dokka(project(":gradle-plugin"))
}

tasks.withType<KotlinNpmInstallTask>().configureEach {
    if (name == "kotlinWasmNpmInstall")
        mustRunAfter("kotlinNpmInstall")
}

tasks.named<UpdateDaemonJvm>("updateDaemonJvm") {
    languageVersion = JavaLanguageVersion.of(24)
}

dokka {
    moduleName = "Inspekt-parent"
}