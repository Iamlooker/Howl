apply("$rootDir/android-library.gradle")

val implementation by configurations

dependencies {
	implementation(project(Modules.constants))
	implementation(project(Modules.coreModel))
}