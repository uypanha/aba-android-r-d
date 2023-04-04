@file:Suppress("UnstableApiUsage")

include(":feature:markdown")


pluginManagement {

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

includeBuild("build-logic")

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "R&D App"
include(":app")
include(":core:network")
include(":core:data")
include(":core:designsystem")
include(":core:model")
include(":core:common")
include(":core:datastore")

include(":feature:topic")
include(":feature:sse")
include(":feature:mini-app")
include(":feature:ml-kits")

include(":open-cv")