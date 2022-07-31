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

include(
	":app",
	":core-common",
	":core-data",
	":core-database",
	":core-model",
	":core-navigation",
	":core-service",
	":core-ui",
	":data-music",
	":feature-album",
	":feature-player",
	":feature-song",
	":music",
	":music:music-data",
	":on-boarding"
)