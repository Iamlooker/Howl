apply("$rootDir/android-library.gradle")

val implementation by configurations

dependencies {
	implementation(project(Modules.constants))
	implementation(project(Modules.components))
	implementation(project(Modules.domainMusic))

	implementation(Media3.media3Session)
	implementation(Media3.media3ExoPlayer)
	implementation(project(mapOf("path" to ":app")))
}