group = "hu.kotlin.feladat.ms"

plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    id("org.springframework.boot")
}

repositories {
    mavenCentral()
}

dependencies {
    val springCloudVersion: String by project
    val mockitoVersion: String by project

    implementation(platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-web")
    implementation(group = "org.springframework.cloud", name = "spring-cloud-starter-openfeign", version = springCloudVersion)
    implementation(group = "com.fasterxml.jackson.module", name = "jackson-module-kotlin")

    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-params")

    testImplementation(kotlin("test"))
    testImplementation(group = "org.mockito", name = "mockito-core", version = mockitoVersion)
    testImplementation(group = "org.mockito", name = "mockito-junit-jupiter", version = mockitoVersion)
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}
