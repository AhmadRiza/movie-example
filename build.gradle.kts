import com.android.build.gradle.tasks.MergeResources
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType
import org.jlleitschuh.gradle.ktlint.tasks.BaseKtLintCheckTask

buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven(url = "https://plugins.gradle.org/m2/")
        maven(url = "https://jitpack.io")
    }
    dependencies {
        classpath(libs.classpath.android.gradle)
        classpath(libs.classpath.kotlin.gradle)
        classpath(libs.classpath.kotlin.serialization)
        classpath(libs.classpath.kotlin.allopen)
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")

    }
}

// IDE bug https://youtrack.jetbrains.com/issue/KTIJ-19369
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    jacoco
    alias(libs.plugins.doctor)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.test.retry)
    alias(libs.plugins.cache.fix) apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
        maven(url = "https://androidx.dev/storage/compose-compiler/repository/")
    }
}

configurations.all {
    resolutionStrategy {
        eachDependency {
            when {
                requested.group == "org.jetbrains.kotlin" -> useVersion(libs.versions.kotlin.get())
                requested.name == "kotlinx-coroutines-core" -> {
                    useVersion(libs.versions.coroutines.get())
                }
                else -> {}
            }
        }
    }
}

// Setup Gradle Doctor
doctor {
    warnWhenJetifierEnabled.set(true)
    warnWhenNotUsingParallelGC.set(false)
    enableTestCaching.set(false)
    javaHome {
        failOnError.set(false)
    }
}

apply(from = "$rootDir/jacoco-ignore-list.gradle")

// Setup Test Retry Plugin & Enforce Java 11 to kotlin module
subprojects {
    apply(plugin = "org.gradle.test-retry")

    tasks.withType<Test>().configureEach {
        maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).takeIf { it > 0 } ?: 1
        setForkEvery(100)

        testLogging {
            events("passed", "skipped", "failed")
        }
        reports.html.required.set(true)
        reports.junitXml.required.set(true)
    }

    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "11"
            freeCompilerArgs = freeCompilerArgs + listOf(
                "-P",
                "plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck=true",
                // "-Xuse-k2"
            )
        }
    }

    plugins.withType<com.android.build.gradle.api.AndroidBasePlugin> {
        apply(plugin = "org.gradle.android.cache-fix")
    }

    tasks.withType(MergeResources::class.java).configureEach {
        outputs.cacheIf { false }
    }

    afterEvaluate {
        val jacocoIgnoreList: List<String> by rootProject.extra
        if ((isAndroidModule(this) || isKotlinModule(this)) &&
            path !in jacocoIgnoreList
        ) {
            apply(plugin = "com.riza.example.jacoco.config")
        }
    }
}

// Setup Jacoco
jacoco {
    toolVersion = libs.versions.jacoco.get()
}

tasks.register("jacocoFullReport", JacocoReport::class.java) {
    group = "Coverage reports"

    val jacocoIgnoreList: List<String> by rootProject.extra
    val validProjects = subprojects.filter {
        (isAndroidModule(it) || isKotlinModule(it)) && !jacocoIgnoreList.contains(it.path)
    }

    val jacocoTestReportTasks = validProjects.map {
        it.tasks.withType(JacocoReport::class).first()
    }

    setDependsOn(jacocoTestReportTasks)

    val testReportSourceDirectories = jacocoTestReportTasks.map { files(it.sourceDirectories) }
    val testReportClassDirectories = jacocoTestReportTasks.map { files(it.classDirectories) }
    val testReportExecutionData = jacocoTestReportTasks.map { files(it.executionData) }
    val testReportSourceSets = testReportSourceDirectories.map { it.files }.flatten()

    additionalClassDirs.setFrom(testReportSourceDirectories)
    sourceDirectories.setFrom(testReportSourceDirectories)
    classDirectories.setFrom(testReportClassDirectories)
    executionData.setFrom(testReportExecutionData)

    reports {
        html.required.set(true)
        html.outputLocation.set(file("$buildDir/reports/jacoco/html"))

        xml.required.set(true)
        xml.outputLocation.set(file("$buildDir/reports/jacoco/jacocoFullReport.xml"))
    }
}

// Setup Ktlint
subprojects {
    afterEvaluate {
        if (isAndroidModule(this) || isKotlinModule(this)) {
            apply(plugin = "org.jlleitschuh.gradle.ktlint")

            ktlint {
                version.set("0.43.0")
                android.set(true)
                reporters {
                    reporter(ReporterType.CHECKSTYLE)
                }
                ignoreFailures.set(true)
                filter {
                    exclude {
                        it.file.path.contains("generated/")
                    }
                    exclude {
                        it.file.path.contains("databinding/")
                    }
                }
            }

            tasks.withType<BaseKtLintCheckTask>().configureEach {
                workerMaxHeapSize.set("384m")
            }
        }
    }
}

// Setup task to run all available UI Test
apply(from = "jacoco-ui-test-list.gradle")
tasks.register("runAllUITest") {
    group = "verification"
    val jacocoUITestList: List<String> by rootProject.extra
    val dependOnList = jacocoUITestList.map { "$it:connectedDebugAndroidTest" }
    setDependsOn(dependOnList)
}

tasks.register("prepareAllUITestApk") {
    group = "build"
    val jacocoUITestList: List<String> by rootProject.extra
    setDependsOn(jacocoUITestList.map { "$it:assembleDebugAndroidTest" })
}

tasks.register("clean", Delete::class.java) {
    setDelete(rootProject.buildDir)
}

fun isAndroidModule(project: Project): Boolean {
    val isAndroidLibrary = project.plugins.hasPlugin("com.android.library")
    val isAndroidApp = project.plugins.hasPlugin("com.android.application")
    val isAndroidFeature = project.plugins.hasPlugin("com.android.dynamic-feature")

    return isAndroidLibrary || isAndroidApp || isAndroidFeature
}

fun isKotlinModule(project: Project): Boolean {
    return project.plugins.hasPlugin("kotlin")
}
