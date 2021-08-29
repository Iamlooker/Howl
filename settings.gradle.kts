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
include(":core")

include(":music")
include(":music:ui-songs")
include(":music:data-songs")
include(":music:data-albums")
include(":music:ui-albums")
include(":on-boarding")
