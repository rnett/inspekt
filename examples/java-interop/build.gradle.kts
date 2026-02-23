plugins {
    kotlin("jvm")
    id("dev.rnett.inspekt")
    application
}

application {
    mainClass.set("dev.rnett.inspekt.example.MainKt")
}
