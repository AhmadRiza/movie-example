plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
}

dependencies {
    implementation(libs.classpath.android.gradle)
    implementation(libs.classpath.kotlin.gradle)
    // implementation(libs.classpath.ksp.gradle)
}

gradlePlugin {
    plugins {
        create("module-plugin") {
            id = "com.riza.example.module"
            implementationClass = "com.riza.example.plugins.LibraryPlugin"
        }

        create("kapt-setting-plugin") {
            id = "com.riza.example.kapt"
            implementationClass = "com.riza.example.plugins.KaptSettingsPlugin"
        }
        create("dagger-kapt-setting-plugin") {
            id = "com.riza.example.dagger.kapt"
            implementationClass = "com.riza.example.plugins.DaggerKaptSettingsPlugin"
        }
        create("jacoco-setting-plugin") {
            id = "com.riza.example.jacoco.config"
            implementationClass = "com.riza.example.plugins.JacocoSettingsPlugin"
        }
    }
}
