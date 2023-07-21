plugins {
    id("com.android.library")
    kotlin("android")
    id("com.riza.example.module")
}

android {
    namespace = "com.riza.example.commonui"
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
        useLiveLiterals = false
    }
}

dependencies {
    implementation(libs.kotlin)
    implementation(libs.android.core)
    implementation(libs.lifecycle.viewmodel.ktx)

    implementation(libs.appcompat)

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.compose.foundation)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.constraintlayout)
    implementation(libs.compose.coil)
    implementation(libs.compose.accompanist.placeholder)
    debugImplementation(libs.compose.ui.tooling)
}
