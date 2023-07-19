@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("com.riza.example.module")
    id("com.riza.example.kapt")
    id("com.riza.example.dagger.kapt")
    alias(libs.plugins.anvil)
}

android {
    namespace = "com.riza.example.explore.ui"
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
        useLiveLiterals = false
    }
    defaultConfig {
        testOptions.animationsDisabled = true
    }

    testOptions {
        unitTests.all { it.useJUnitPlatform() }
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:common-ui"))
    implementation(project(":explore:api"))

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
    implementation(libs.compose.activity)
    debugImplementation(libs.compose.ui.tooling)

    implementation(libs.javax.inject)
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)
}