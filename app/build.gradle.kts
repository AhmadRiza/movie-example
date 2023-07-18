import com.android.build.gradle.internal.feature.BundleAllClasses

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
    id("com.riza.example.kapt")
    id("com.riza.example.dagger.kapt")
    alias(libs.plugins.anvil)
}

tasks.withType(BundleAllClasses::class.java).configureEach {
    outputs.cacheIf { false }
}

android {
    namespace = "com.riza.example"
    compileSdk = libs.versions.androidCompileSdk.get().toInt()
    buildToolsVersion = libs.versions.androidBuildTools.get()

    defaultConfig {
        applicationId = "com.riza.example"
        minSdk = libs.versions.androidMinSdk.get().toInt()
        targetSdk = libs.versions.androidTargetSdk.get().toInt()
        versionCode = 1
        versionName = "0.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    android {
        testOptions {
            unitTests.all { it.useJUnitPlatform() }
        }
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file("../debug.keystore")
            storePassword = "riza.com"
            keyAlias = "rizadebug"
            keyPassword = "riza.com"
        }
        create("release") {
            // todo replace with release keystore
            storeFile = file("../debug.keystore")
            // todo replace with System ENV
            storePassword = "riza.com"
            keyAlias = "rizadebug"
            keyPassword = "riza.com"
        }
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".staging"
            isMinifyEnabled = false
            isShrinkResources = false
            isCrunchPngs = false
            isTestCoverageEnabled = project.hasProperty("enableCoverage")
            signingConfig = signingConfigs.getByName("debug")
        }

        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            isTestCoverageEnabled = false
            signingConfig = signingConfigs.getByName("debug") // todo change with release
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    // Tips to add test case, change the test build type to "debug" first and after done
    // change it again to "staging"
    testBuildType = "debug"

    lint {
        checkReleaseBuilds = false
        abortOnError = false
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
        useLiveLiterals = false
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    packagingOptions {
        resources.excludes.addAll(
            listOf(
                "META-INF/AL2.0",
                "META-INF/LGPL2.1",
            )
        )
    }
}

dependencies {
    implementation(project(":common"))
    implementation(project(":cache"))
    implementation(project(":network"))

    // UI
    implementation(libs.appcompat)
    implementation(libs.fragment.ktx)
    implementation(libs.google.material)

    // Compose
    implementation(libs.compose.ui)
    implementation(libs.compose.material)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.activity)
    implementation(libs.compose.coil)
    implementation(libs.compose.runtime)
    implementation(libs.compose.constraintlayout)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.compose.accompanist.placeholder)

    implementation(libs.android.core)
    implementation(libs.android.core.ktx)

    // Kotlin
    implementation(libs.kotlin)
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    // Android Architecture Components
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.livedata.core)
    implementation(libs.lifecycle.livedata.core.ktx)

    // DI
    implementation(libs.javax.inject)
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)

    // Needed for DI classpath resolve
    implementation(libs.retrofit)

    // Memory Leak Detection
    debugImplementation(libs.leakcanary)

    // Serialization
    implementation(libs.gson)

    coreLibraryDesugaring(libs.desugar) {
        because("Needed for java.time compatibility on os below API 26")
    }

    // Instrumental Test
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.espresso.intents)
    androidTestImplementation(libs.fragment.testing)
    androidTestImplementation(libs.test.junit.ktx)
    androidTestImplementation(libs.test.runner)
    androidTestImplementation(libs.test.rules)
    androidTestImplementation(libs.test.core)
    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.dexmaker)
    androidTestImplementation(libs.barista) {
        exclude(group = "org.jetbrains.kotlin")
    }

    debugImplementation(libs.compose.ui.test.manifest)
    androidTestImplementation(libs.compose.ui.test.junit4)
    androidTestImplementation(libs.arch.core.testing)

    // unit test
    testImplementation(libs.coroutines.test)
    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.arch.core.testing)
    testImplementation(libs.mockk)
    testImplementation("org.json:json:20220320") {
        because("Needed to test JSONObject")
    }
}
