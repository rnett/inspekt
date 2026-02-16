plugins {
    id("internal")
    id("kotlin-jvm")
    alias(libs.plugins.kcp.dev.compiler)
}

compilerPluginDevelopment {
    compilerPluginRegistrar = "dev.rnett.spekt.Registrar"
    commandLineProcessor = "dev.rnett.spekt.CliProcessor"
//    testGenerator = "dev.rnett.spekt.TestGenerator"
}

dependencies {
    compilerTestRuntimeClasspath(project(":api"))
}