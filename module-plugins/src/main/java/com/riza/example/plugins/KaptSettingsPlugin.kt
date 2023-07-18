package com.riza.example.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KaptExtension

class KaptSettingsPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val kaptPlugin = project.extensions.findByName("kapt") as? KaptExtension ?: error(
            "Plugin must be applied after applying the kotlin-kapt plugin"
        )

        kaptPlugin.useBuildCache = true
        // kaptPlugin.correctErrorTypes = false
    }
}
