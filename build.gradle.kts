val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    base
    kotlin("jvm") version "1.6.20"
}
allprojects {

    apply(plugin = "org.jetbrains.kotlin.jvm")

    group = "ge.nika"
    version = "0.0.1"

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation(kotlin("reflect"))

        testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
        testImplementation("io.mockk:mockk:1.12.3")
        testImplementation("io.kotest:kotest-runner-junit5:5.2.3")
        testImplementation("io.kotest:kotest-assertions-core:5.2.3")
    }
}


