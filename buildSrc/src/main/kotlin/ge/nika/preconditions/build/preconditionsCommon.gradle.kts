package ge.nika.preconditions.build

plugins {
    kotlin("jvm")
}

val ragaca = "ragaca"

dependencies {
    implementation(kotlin("reflect"))

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:2.0.21")
    testImplementation("io.mockk:mockk:1.12.3")
    testImplementation("io.kotest:kotest-runner-junit5:5.2.3")
    testImplementation("io.kotest:kotest-assertions-core:5.2.3")
}

kotlin {
    jvmToolchain(17)
}