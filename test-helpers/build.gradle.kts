plugins {
    id("internal")
    id("kotlin-jvm")
}

dependencies {
    api(libs.kotlinx.coroutines.test)
    api(project(":spekt"))
    api(kotlin("test-junit5"))
}