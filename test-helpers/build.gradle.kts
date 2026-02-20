plugins {
    id("internal")
    id("kotlin-jvm")
}

dependencies {
    api(libs.kotlinx.coroutines.test)
    api(project(":inspekt"))
    api(kotlin("test-junit5"))
}