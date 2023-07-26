plugins {
    id("com.android.library")
    kotlin("android")
    id("com.riza.example.module")
}

android {
    namespace = "com.riza.example.detail.api"
}

dependencies {
    implementation(project(":core:common"))

    implementation(libs.kotlin)
    implementation(libs.android.annotations)
}
