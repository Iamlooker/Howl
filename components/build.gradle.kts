apply ("$rootDir/android-library.gradle")

val implementation by configurations

dependencies {
    implementation(project(Modules.core))
    implementation(Libs.Palette.palette)
    implementation(Libs.Coil.coil)
}