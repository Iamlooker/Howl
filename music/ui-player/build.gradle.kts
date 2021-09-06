apply("$rootDir/android-library.gradle")

val implementation by configurations

dependencies {
    implementation(Palette.palette)
    implementation(Coil.coil)
}