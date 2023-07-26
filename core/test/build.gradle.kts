plugins {
    id("com.android.library")
    kotlin("android")
    id("com.riza.example.module")
}

android {
    namespace = "com.riza.example.test"
}

dependencies {
    implementation(project(":core:common"))
    implementation(libs.lifecycle.livedata.core.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.test)
    implementation(libs.junit)
    implementation(libs.kotest.runner.junit5)
    implementation(libs.arch.core.testing)
    implementation(libs.kotlin.junit)
    implementation(libs.mockk.android)
}
