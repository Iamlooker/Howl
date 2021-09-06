apply("$rootDir/android-library.gradle")

val implementation by configurations

dependencies {
    implementation(project(Modules.components))
    implementation(Palette.palette)
    implementation(Coil.coil)
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.0-beta02")
}