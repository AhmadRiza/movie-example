package com.riza.example.plugins

import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.DynamicFeatureExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

class LibraryPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val androidPlugin = project.androidPlugin()
            ?: error(
                "Plugin must be applied after applying the application, " +
                    "library, or dynamic-feature Android plugin"
            )

        val versionCatalog = project.extensions.getByType<VersionCatalogsExtension>()
            .named("libs")

        when (androidPlugin) {
            is AndroidPlugin.App -> {
                /** TODO */
            }
            is AndroidPlugin.DynamicFeature -> {
                configureDynamicFeature(androidPlugin.androidExtension, versionCatalog, project)
            }
            is AndroidPlugin.Library -> {
                configureLibrary(androidPlugin.androidExtension, versionCatalog, project)
            }
        }
    }

    private fun configureDynamicFeature(
        dynamicFeatureExtension: DynamicFeatureExtension,
        versionCatalog: VersionCatalog,
        project: Project
    ) {
        with(dynamicFeatureExtension) {
            compileSdk = versionCatalog.compileSdkVersion()
            buildToolsVersion = versionCatalog.buildToolsVersion()

            defaultConfig {
                minSdk = versionCatalog.minSdkVersion()
            }

            buildTypes {
                getByName("debug") {
                    // Run jacocoTestReport with `enableCoverage` property,
                    // e.g ./gradlew [module]:jacocoTestReport -PenableCoverage
                    isTestCoverageEnabled = project.hasProperty("enableCoverage")
                }
            }

            lint {
                checkReleaseBuilds = false
                abortOnError = false
                disable.add("NullSafeMutableLiveData")
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_11
                targetCompatibility = JavaVersion.VERSION_11
            }

            (this as ExtensionAware).extensions.configure<KotlinJvmOptions>("kotlinOptions") {
                jvmTarget = "11"
                freeCompilerArgs = freeCompilerArgs + listOf(
                    "-opt-in=kotlin.time.ExperimentalTime"
                )
            }

            // This part will solve missing coverage, cov become 0%
            // issue https://example.com/jacoco/jacoco/issues/996
            if (project.hasProperty("enableCoverage")) {
                sourceSets {
                    sourceSets["test"].java.srcDirs(
                        "${project.projectDir}/src/test/java",
                        "${project.projectDir}/src/main/java"
                    )
                }
            }
        }
    }

    private fun configureLibrary(
        libraryExtension: LibraryExtension,
        versionCatalog: VersionCatalog,
        project: Project
    ) {
        with(libraryExtension) {
            compileSdk = versionCatalog.compileSdkVersion()
            buildToolsVersion = versionCatalog.buildToolsVersion()

            defaultConfig {
                minSdk = versionCatalog.minSdkVersion()
                targetSdk = versionCatalog.targetSdkVersion()
            }

            buildTypes {
                getByName("debug") {
                    // Run jacocoTestReport with `enableCoverage` property,
                    // e.g ./gradlew [module]:jacocoTestReport -PenableCoverage
                    isTestCoverageEnabled = project.hasProperty("enableCoverage")
                }
            }

            lint {
                checkReleaseBuilds = false
                abortOnError = false
                disable.add("NullSafeMutableLiveData")
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_11
                targetCompatibility = JavaVersion.VERSION_11
            }

            kotlinOptions {
                jvmTarget = "11"
                freeCompilerArgs = freeCompilerArgs + listOf(
                    "-opt-in=kotlin.time.ExperimentalTime"
                )
            }

            // This part will solve missing coverage, cov become 0%
            // issue https://example.com/jacoco/jacoco/issues/996
            if (project.hasProperty("enableCoverage")) {
                sourceSets {
                    sourceSets["test"].java.srcDirs(
                        "${project.projectDir}/src/test/java",
                        "${project.projectDir}/src/main/java"
                    )
                }
            }
        }
    }

    fun LibraryExtension.kotlinOptions(configure: KotlinJvmOptions.() -> Unit): Unit =
        (this as ExtensionAware).extensions.configure("kotlinOptions", configure)

    private fun VersionCatalog.compileSdkVersion() = getIntVersion("androidCompileSdk")

    private fun VersionCatalog.buildToolsVersion() = getStringVersion("androidBuildTools")

    private fun VersionCatalog.minSdkVersion() = getIntVersion("androidMinSdk")

    private fun VersionCatalog.targetSdkVersion() = getIntVersion("androidTargetSdk")

    private fun VersionCatalog.getIntVersion(name: String): Int {
        return getStringVersion(name).toInt()
    }

    private fun VersionCatalog.getStringVersion(name: String): String {
        return findVersion(name).get().toString()
    }
}
