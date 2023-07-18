import java.net.URI

rootProject.name = "Verification Example"

pluginManagement {
    includeBuild("./module-plugins")

    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "com.riza.example.module") {
                useModule("com.android.tools.build:gradle:7.2.2")
            }
        }
    }
}

include(
    ":app", ":network", ":cache", ":common",
)