plugins {
    ge.nika.preconditions.build.preconditionsCommon
    id("com.gradleup.shadow") version "9.0.0-beta11"
}

apply(plugin = "com.gradleup.shadow")

dependencies {
    implementation(project(":library"))

    implementation(platform("org.http4k:http4k-bom:4.25.13.0"))
    implementation("org.http4k:http4k-core")
    implementation("org.http4k:http4k-server-netty")
    implementation("org.http4k:http4k-client-apache")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.2.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.2")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "ge.nika.preconditions.testApp.MainKt"
    }
}