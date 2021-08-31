dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "HowlMusic"

include(":app")
include(":constants")
include(":components")

include(":music")
include(":music:ui-songs")
include(":music:data-music")
include(":music:ui-albums")
include(":on-boarding")
