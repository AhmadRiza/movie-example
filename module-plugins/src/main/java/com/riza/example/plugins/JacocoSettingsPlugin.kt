package com.riza.example.plugins

import com.android.build.gradle.api.SourceKind
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.the
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension
import org.gradle.testing.jacoco.tasks.JacocoReport
import java.io.File

class JacocoSettingsPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.plugins.apply("jacoco")

        project.apply(from = "${project.rootDir}/jacoco-ignore-list.gradle")
        project.apply(from = "${project.rootDir}/jacoco-ui-test-list.gradle")

        val versionCatalog = project.extensions.getByType<VersionCatalogsExtension>()
            .named("libs")

        val jacoco = project.extensions.findByName("jacoco") as? JacocoPluginExtension ?: error(
            "Plugin must be applied after applying the jacoco plugin"
        )

        jacoco.toolVersion = versionCatalog.findVersion("jacoco").get().toString()

        project.afterEvaluate {
            val jacocoIgnoreList: List<String> by project.rootProject.extra
            val projectName = project.path

            if (projectName in jacocoIgnoreList) {
                println("Jacoco: ignoring project $projectName")
            }

            when {
                project.androidPlugin() != null -> {
                    setupAndroidReporting(project)
                }
                project.plugins.hasPlugin("kotlin") -> {
                    setupKotlinReporting(project)
                }
            }
        }
    }

    private fun setupKotlinReporting(project: Project) {
        project.tasks.named("jacocoTestReport", JacocoReport::class.java).configure {
            dependsOn("test")

            reports {
                csv.required.set(false)
                xml.required.set(true)
                html.required.set(true)
            }

            project.the<SourceSetContainer>()["main"]

            executionData.setFrom(
                project.fileTree(project.buildDir).include("/jacoco/*.exec")
            )
        }
    }

    private fun setupAndroidReporting(project: Project) {
        val jacocoUITestList: List<String> by project.extra

        val testTask = "testDebugUnitTest"
        val variantName = "debug"
        val uiTestTask = "connectedDebugAndroidTest"

        val taskDepends = if (jacocoUITestList.contains(project.path)) {
            listOf(testTask, uiTestTask)
        } else {
            listOf(testTask)
        }

        project.tasks.withType(Test::class).configureEach {
            configure<JacocoTaskExtension> {
                isEnabled = true
                excludes = listOf("jdk.internal.*")
                isIncludeNoLocationClasses = false
            }
        }

        if (project.tasks.findByName("jacocoTestReport") == null) {
            project.tasks.register("jacocoTestReport", JacocoReport::class) {
                dependsOn(taskDepends)

                group = "Coverage reports"

                reports {
                    csv.required.set(false)
                    xml.required.set(true)
                    html.required.set(true)
                }

                val fileFilter = listOf(
                    "**/R.class",
                    "**/R$*.class",
                    "**/*\$ViewInjector*.*",
                    "**/*\$ViewBinder*.*",
                    "**/BuildConfig.*",
                    "**/Manifest*.*",
                    "**/BR.class",
                    "**/BR$*.class",
                    "**/databinding/**/*.*",
                    "**/DataBinderMapper*.*",
                    "**/*App.*",
                    "**/*Application.*",
                    "**/*Component*.*",
                    "**/*ComponentFactory.*",
                    "**/*Module.*",
                    "android/**/*.*",
                    "**/*ItemDecoration.*",
                    "**/*ViewHolder.*",
                    "**/*ViewModelFactory.*",
                    "**/*Test.*",
                    "**/*Test$*.*",
                    "**/*KotlinExtensionKt.*",
                )

                sourceDirectories.setFrom(
                    project.files("${project.projectDir}/src/main/java")
                )

                classDirectories.setFrom(
                    project.files(
                        project.fileTree(
                            "${project.buildDir}/tmp/kotlin-classes/$variantName"
                        ).exclude(fileFilter)
                    )
                )

                executionData.setFrom(
                    project.fileTree(project.buildDir).include(
                        "outputs/unit_test_code_coverage/${variantName}UnitTest/$testTask.exec",
                        "outputs/code_coverage/${variantName}AndroidTest/connected/*coverage.ec"
                    )
                )
            }
        }
    }
}
