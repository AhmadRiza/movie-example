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

@Suppress("UnstableApiUsage")
class LibraryPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val androidPlugin = project.androidPlugin()
            ?: error(
                "Plugin must be applied after applying the application, " +
                        "library, or dynamic-feature Android plugin"
            )

        project.plugins.apply("org.gradle.android.cache-fix")

        val versionCatalog = project.extensions.getByType<VersionCatalogsExtension>()
            .named("libs")

        when (androidPlugin) {
            is AndroidPlugin.App -> {
                /* TODO */
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
                    enableUnitTestCoverage = project.hasProperty("enableCoverage")
                }
            }

            lint {
                checkReleaseBuilds = false
                abortOnError = false
                disable.add("NullSafeMutableLiveData")
                ignoreTestSources = true
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }

            (this as ExtensionAware).extensions.configure<KotlinJvmOptions>("kotlinOptions") {
                jvmTarget = "17"
                freeCompilerArgs = freeCompilerArgs + listOf(
                    "-opt-in=kotlin.time.ExperimentalTime",
                    "-P",
                    "plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck=true",
                )
            }

            packagingOptions {
                resources.excludes.addAll(
                    listOf(
                        "META-INF/AL2.0",
                        "META-INF/LGPL2.1",
                        "*.kotlin_module"
                    )
                )
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
                    enableUnitTestCoverage = project.hasProperty("enableCoverage")
                }
            }

            lint {
                checkReleaseBuilds = false
                abortOnError = false
                disable.add("NullSafeMutableLiveData")
                ignoreTestSources = true
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }

            kotlinOptions {
                jvmTarget = "17"
                freeCompilerArgs = freeCompilerArgs + listOf(
                    "-opt-in=kotlin.time.ExperimentalTime",
                    "-P",
                    "plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck=true",
                )
            }

            packagingOptions {
                resources.excludes.addAll(
                    listOf(
                        "META-INF/AL2.0",
                        "META-INF/LGPL2.1",
                        "*.kotlin_module"
                    )
                )
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