apply("$rootDir/android-library.gradle")

val implementation by configurations
val kapt by configurations

dependencies {
    implementation(project(Modules.core))
    implementation(Libs.Room.room)
    kapt(Libs.Room.roomCompiler)
}