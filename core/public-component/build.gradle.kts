plugins {
    id("com.android.library")
    kotlin("android")
    id("com.riza.example.module")
}

android {
    namespace = "com.riza.example.publiccomponent"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":explore:ui"))
    implementation(project(":detail:ui"))
    implementation(libs.kotlin)
}
