import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.3.20"
    id("com.google.cloud.tools.jib") version "1.0.2"
    id("kotlinx-serialization") version ("1.3.0")
}

group = "com.github.sakebook"
version = "0.0.1"

repositories {
    mavenCentral()
    jcenter()
    maven("https://dl.bintray.com/kotlin/ktor")
    maven("https://kotlin.bintray.com/kotlinx")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.ktor", "ktor-client-core", "1.1.3")
    implementation("io.ktor", "ktor-client-core-jvm", "1.1.3")
    implementation("io.ktor", "ktor-client-json", "1.1.3")
    implementation("io.ktor", "ktor-client-json-jvm", "1.1.3")
    implementation("io.ktor", "ktor-client-okhttp", "1.1.3")
    implementation("org.jetbrains.kotlinx", "kotlinx-serialization-runtime", "0.10.0")
    testImplementation("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}