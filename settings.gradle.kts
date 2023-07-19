import java.net.URI

rootProject.name = "verification-form"

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
    ":app", ":core:network", ":core:cache", ":core:common",  ":core:common-ui",
    ":core:public-component",
    ":auth:ui", ":auth:api"
)