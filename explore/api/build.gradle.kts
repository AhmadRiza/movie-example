plugins {
    id("com.android.library")
    kotlin("android")
    id("com.riza.example.module")
    id("kotlin-parcelize")
}

android {
    namespace = "com.riza.example.explore.api"
}

dependencies {
    implementation(project(":core:common"))

    implementation(libs.kotlin)
    implementation(libs.android.annotations)
}
