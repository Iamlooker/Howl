apply("$rootDir/android-library.gradle")

val implementation by configurations

dependencies {
	implementation(project(Modules.coreModel))
	implementation(project(Modules.components))
	implementation(Coil.coil)
}