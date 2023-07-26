// IDE bug https://youtrack.jetbrains.com/issue/KTIJ-19369
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.plugin.allopen")
    kotlin("android")
    kotlin("kapt")
    id("kotlinx-serialization")
    id("com.riza.example.module")
    id("com.riza.example.kapt")
    id("com.riza.example.dagger.kapt")
    alias(libs.plugins.anvil)
}

android {
    namespace = "com.riza.example.explore.service"
    testOptions {
        unitTests.all { it.useJUnitPlatform() }
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:network"))

    // DI
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)

    // Kotlin
    implementation(libs.kotlin)
    implementation(libs.kotlin.datetime)
    implementation(libs.coroutines.core)

    // General
    implementation(libs.android.annotations)

    // Networking
    implementation(libs.retrofit)
    implementation(libs.gson)

    testImplementation(libs.mockk)
    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.coroutines.test)
}
