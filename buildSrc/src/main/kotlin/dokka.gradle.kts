import org.jetbrains.dokka.gradle.DokkaExtension
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