import build.extensionIfPresent
import build.gradlePublishing
import build.mavenPublications
import build.mavenPublicationsWithoutPluginMarker
import com.vanniktech.maven.publish.MavenPublishBaseExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins {
    id("com.vanniktech.maven.publish.base")
    id("dokka")
}

extensionIfPresent<MavenPublishBaseExtension> {
    publishToMavenCentral(automaticRelease = true)
    signAllPublications()
    coordinates(group.toString(), project.name, project.version.toString())

    pom {
        name = "Klif - ${project.name}"
        description = "A Kotlin Multiplatform logging facade"
        inceptionYear = "2025"
        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                distribution = "http://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }
        developers {
            developer {
                id = "rnett"
                name = "Ryan Nett"
                url = "https://github.com/rnett/"
            }
        }
        scm {
            url = "https://github.com/rnett/spekt/"
            connection = "scm:git:git://github.com/rnett/inspekt.git"
            developerConnection = "scm:git:ssh://git@github.com/rnett/inspekt.git"
        }
    }
}

fun registerDokkaJavadocTask(prefix: String? = null): TaskProvider<Jar> {

    val name = if (prefix == null) "dokkaJavadocJar" else prefix + "DokkaJavadocJar"

    return tasks.register<Jar>(name) {
        val task = tasks.named("dokkaGeneratePublicationHtml")
        dependsOn(task)
        from(task)
        archiveClassifier.set("javadoc")

        if (prefix != null) {
            archiveBaseName.set("${project.name}-$prefix")
        }
    }
}

plugins.withId("org.gradle.java-test-fixtures") {
    val component = components["java"] as AdhocComponentWithVariants
    component.withVariantsFromConfiguration(configurations["testFixturesApiElements"]) { skip() }
    component.withVariantsFromConfiguration(configurations["testFixturesRuntimeElements"]) { skip() }
}

plugins.withId("com.gradleup.shadow") {
    tasks.named { it == "shadowJar" }.configureEach {
        dependsOn("jar")
    }
}

afterEvaluate {
    val hasGradlePlugin = project.plugins.hasPlugin("java-gradle-plugin")
    val hasKotlinJvm = project.plugins.hasPlugin("org.jetbrains.kotlin.jvm")
    val hasKotlinMultiplatform = project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform")
    val hasShadowPlugin = project.plugins.hasPlugin("com.gradleup.shadow")

    if (hasGradlePlugin) {
        // we use our own publication instead
        tasks.named { "PluginMavenPublication" in it }.configureEach {
            enabled = false
        }
    }

    when {
        hasKotlinJvm -> {
            gradlePublishing.publications.register<MavenPublication>("maven") {
                val componentName = if (hasShadowPlugin) "shadow" else "java"

                from(project.components.getByName(componentName))
            }

            extensionIfPresent<JavaPluginExtension> {
                withSourcesJar()
            }

            val javadocTask = registerDokkaJavadocTask()
            mavenPublicationsWithoutPluginMarker {
                artifact(javadocTask)
            }
        }

        hasKotlinMultiplatform -> {
            mavenPublications {
                artifact(registerDokkaJavadocTask(this.name))
            }
            extensionIfPresent<KotlinMultiplatformExtension> {
                withSourcesJar(true)
            }
        }
    }
}