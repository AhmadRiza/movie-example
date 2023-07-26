
rootProject.name = "movie-example"

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
                useModule("com.android.tools.build:gradle:8.0.2")
            }
        }
    }
}

include(
    ":app", ":core:network", ":core:cache", ":core:common", ":core:common-ui",
    ":core:public-component", "core:test",
    ":explore:ui", ":explore:api", ":explore:service",
    ":detail:ui", ":detail:api", ":detail:service"
)
