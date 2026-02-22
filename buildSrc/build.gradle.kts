plugins {
    `kotlin-dsl`
}

dependencies {
    api(plugin(libs.plugins.kotlin.multiplatform))
    api(plugin(libs.plugins.kotlin.jvm))
    api(plugin(libs.plugins.kotlin.power.assert))
    api(plugin(libs.plugins.dokka))
    api(plugin(libs.plugins.publishing))
}

// Helper function that transforms a Gradle Plugin alias from a
// Version Catalog into a valid dependency notation for buildSrc
fun DependencyHandlerScope.plugin(plugin: Provider<PluginDependency>) =
    plugin.map { "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version}" }
