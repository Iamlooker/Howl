apply("$rootDir/android-library.gradle")

val implementation by configurations

dependencies {
    implementation(project(Modules.components))
    implementation(Palette.palette)
    implementation(Coil.coil)
    implementation(Accompanist.insets)
}