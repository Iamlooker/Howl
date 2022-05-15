apply("$rootDir/android-library.gradle")

val implementation by configurations

dependencies {
	implementation(Core.core)
	implementation(Palette.palette)
	implementation(Coil.coil)
}