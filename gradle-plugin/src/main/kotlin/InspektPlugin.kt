package dev.rnett.inspekt

import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderFactory
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption
import javax.inject.Inject

public class InspektPlugin @Inject constructor(
    private val providers: ProviderFactory
) : KotlinCompilerPluginSupportPlugin {
    override fun apply(target: Project) {
        target.extensions.create("inspekt", InspektExtension::class.java).apply {
            addInspektDependencies.convention(true)
            warnOnDefaultParameters.convention(5)
        }
    }

    override fun applyToCompilation(kotlinCompilation: KotlinCompilation<*>): Provider<List<SubpluginOption>> {
        kotlinCompilation.allKotlinSourceSets.forEach {
            it.languageSettings.optIn(BuildConfig.COMPILER_PLUGIN_INTRINSIC_ANNOTATION)
        }


        val extension = kotlinCompilation.project.extensions.getByType(InspektExtension::class.java)
        extension.addInspektDependencies.finalizeValue()
        if (extension.addInspektDependencies.get()) {
            kotlinCompilation.defaultSourceSet.dependencies {
                implementation(BuildConfig.KOTLIN_PLUGIN_GROUP + ":" + BuildConfig.LIBRARY_ARTIFACT_ID + ":" + BuildConfig.KOTLIN_PLUGIN_VERSION)
            }
        }



        return providers.provider {
            listOf(SubpluginOption("warnOnDefaultArguments", lazy {
                extension.warnOnDefaultParameters.finalizeValue()
                extension.warnOnDefaultParameters.get().toString()
            }))
        }
    }

    override fun getCompilerPluginId(): String = BuildConfig.KOTLIN_PLUGIN_ID

    override fun getPluginArtifact(): SubpluginArtifact = SubpluginArtifact(
        groupId = BuildConfig.KOTLIN_PLUGIN_GROUP,
        artifactId = BuildConfig.KOTLIN_PLUGIN_NAME,
        version = BuildConfig.KOTLIN_PLUGIN_VERSION
    )

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean {
        return true
    }
}

public abstract class InspektExtension {
    /**
     * If true (which is the default), the dependency on the `inspekt` runtime library will be automatically added to all source sets (as an `implementation` dependency).
     * The library is required to invoke `inspekt`, so if it's not included, the compiler plugin is useless.
     */
    public abstract val addInspektDependencies: Property<Boolean>

    /**
     * If greater than 0 (the default is 5), warns when invocation lambas are generated for functions with more default arguments than this number.
     * This is because the invocation lambda has a when statement with a branch for each combination of default arguments, meaning `2 ^ (number of default arguments)` cases.
     * This can easily lead to performance issues and binary size bloat.
     */
    public abstract val warnOnDefaultParameters: Property<Int>

}