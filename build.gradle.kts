import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.22"
    kotlin("plugin.serialization") version "1.7.21"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    `maven-publish`
    application
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    val minestomVersion = "809d9516b2"
    val loggingVersion = "3.0.4"
    val mockkVersion = "1.13.3"
    val coroutinesCoreVersion = "1.6.4"
    val kotlinSerializationVersion = "1.4.1"

    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesCoreVersion")
    implementation("com.github.Minestom.Minestom:Minestom:$minestomVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$kotlinSerializationVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-hocon:$kotlinSerializationVersion")

    // Logging information
    implementation("io.github.microutils:kotlin-logging:$loggingVersion")

    testImplementation(kotlin("test-junit5"))
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesCoreVersion")
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("com.github.Minestom.Minestom:testing:$minestomVersion")
}

kotlin {
    sourceSets {
        all {
            languageSettings {
                optIn("kotlin.RequiresOptIn")
                optIn("kotlin.ExperimentalStdlibApi")
            }
        }
    }
}

tasks {
    test {
        useJUnitPlatform()
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
    }

    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        archiveFileName.set("${project.name}.jar")
    }
}

application {
    mainClass.set("fr.rushy.hub.Main")
}