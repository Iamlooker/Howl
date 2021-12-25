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
include(":music:domain-music")
include(":music:data-music")
include(":music:ui-songs")
include(":music:ui-albums")
include(":music:ui-genre")

include(":player")
include(":player:ui-player")
include(":player:player-service")