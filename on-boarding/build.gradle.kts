apply("$rootDir/android-library.gradle")

val implementation by configurations

dependencies {
	implementation(project(Modules.constants))
	implementation(Compose.runtimeLiveData)
	implementation(Lifecycle.lifecycleViewModelCompose)
}