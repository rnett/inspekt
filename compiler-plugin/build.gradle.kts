plugins {
    id("internal")
    id("kotlin-jvm")
    alias(libs.plugins.kcp.dev.compiler)
    alias(libs.plugins.symbol.export.import)
}

compilerPluginDevelopment {
    compilerPluginRegistrar = "dev.rnett.inspekt.Registrar"
    commandLineProcessor = "dev.rnett.inspekt.CliProcessor"
    testGenerator = "dev.rnett.inspekt.TestGenerator"
}

dependencies {
    implementation(libs.symbols.kotlin.compiler)
    compilerTestRuntimeClasspath(project(":test-helpers"))

    importSymbols(project(":inspekt"))
}

tasks.test {
    maxHeapSize = "3g"
}