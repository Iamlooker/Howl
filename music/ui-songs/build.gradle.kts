apply("$rootDir/android-library.gradle")

val implementation by configurations

dependencies {
    implementation(project(Modules.components))
    implementation(project(Modules.dataMusic))

    implementation(Accompanist.insets)
}