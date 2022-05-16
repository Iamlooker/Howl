pluginManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()

    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "Howl"

include(":on-boarding")

include(":app")

include(":constants")
include(":components")

include(":music")
include(":music:data-music")
include(":music:ui-songs")
include(":music:ui-albums")

include(":ui-player")
include(":core-model")
include(":feature-player")