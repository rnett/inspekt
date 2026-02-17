plugins {
    id("internal")
    id("kotlin-jvm")
    alias(libs.plugins.kcp.dev.compiler)
    alias(libs.plugins.symbol.export.import)
}

compilerPluginDevelopment {
    compilerPluginRegistrar = "dev.rnett.spekt.Registrar"
    commandLineProcessor = "dev.rnett.spekt.CliProcessor"
    testGenerator = "dev.rnett.spekt.TestGenerator"
}

dependencies {
    implementation(libs.symbols.kotlin.compiler)
    compilerTestRuntimeClasspath(project(":spekt"))

    importSymbols(project(":spekt"))
}

tasks.test {
    maxHeapSize = "3g"
}