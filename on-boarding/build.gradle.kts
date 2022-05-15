apply("$rootDir/android-library.gradle")

val implementation by configurations

dependencies {
	implementation(Compose.activity)
	implementation(Compose.runtime)
	implementation(Lifecycle.lifecycleViewModelCompose)
}