plugins {
    alias(libs.plugins.kotlin.jvm) apply false
}

tasks.named<UpdateDaemonJvm>("updateDaemonJvm") {
    languageVersion = JavaLanguageVersion.of(24)
}
