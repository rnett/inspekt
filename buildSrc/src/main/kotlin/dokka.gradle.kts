import org.jetbrains.dokka.gradle.DokkaExtension
import org.jetbrains.dokka.gradle.formats.DokkaFormatPlugin
import org.jetbrains.dokka.gradle.internal.InternalDokkaGradlePluginApi
import java.net.URI

plugins {
    id("org.jetbrains.dokka")
}

val commit: Provider<String> = providers.exec {
    commandLine("git", "rev-parse", "HEAD")
}.standardOutput.asText

the<DokkaExtension>().apply {
    dokkaPublications.configureEach {
        suppressObviousFunctions = true
        suppressInheritedMembers = false
        failOnWarning = true

        if (project.rootProject == project) {
            val moduleFile = layout.projectDirectory.file("Module.md")
            if (moduleFile.asFile.exists())
                includes.from(moduleFile)
        }
    }
    dokkaSourceSets.configureEach {
        sourceLink {
            remoteUrl = commit.map { URI.create("https://github.com/rnett/inspekt/blob/${it.trim()}") }
            localDirectory = project.rootDir
            remoteLineSuffix = "#L"
        }
        suppressGeneratedFiles = true
        jdkVersion = 17

        val moduleFile = layout.projectDirectory.file("Module.md")
        if (moduleFile.asFile.exists())
            includes.from(moduleFile)
    }
}

@OptIn(InternalDokkaGradlePluginApi::class)
abstract class DokkaMarkdownPlugin : DokkaFormatPlugin(formatName = "markdown") {
    override fun DokkaFormatPlugin.DokkaFormatPluginContext.configure() {
        project.dependencies {
            // Sets up current project generation
            dokkaPlugin(dokka("gfm-plugin"))

            // Sets up multi-project generation
            formatDependencies.dokkaPublicationPluginClasspathApiOnly.dependencies.addLater(
                dokka("gfm-template-processing-plugin")
            )
        }
    }
}
apply<DokkaMarkdownPlugin>()