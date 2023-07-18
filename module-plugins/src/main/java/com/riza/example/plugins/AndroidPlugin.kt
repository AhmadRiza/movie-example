package com.riza.example.plugins

import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.DynamicFeatureExtension
import org.gradle.api.Project

internal fun Project.androidPlugin(): AndroidPlugin<*>? =
    when (val androidExtension = extensions.findByName("android")) {
        is DynamicFeatureExtension -> {
            AndroidPlugin.DynamicFeature(androidExtension)
        }
        is AppExtension -> {
            AndroidPlugin.App(androidExtension)
        }
        is LibraryExtension -> {
            AndroidPlugin.Library(androidExtension)
        }

        else -> null
    }

internal sealed class AndroidPlugin<out T : BaseExtension> {

    abstract val androidExtension: T

    class App(
        override val androidExtension: AppExtension,
    ) : AndroidPlugin<AppExtension>()

    class Library(
        override val androidExtension: LibraryExtension,
    ) : AndroidPlugin<LibraryExtension>()

    class DynamicFeature(
        override val androidExtension: DynamicFeatureExtension,
    ) : AndroidPlugin<DynamicFeatureExtension>()
}
