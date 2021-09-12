apply("$rootDir/android-library.gradle")

val implementation by configurations

dependencies {
    implementation(project(Modules.components))
    implementation(project(Modules.uiSongs))
    implementation(project(Modules.dataMusic))
    implementation(Lifecycle.lifecycleViewModelCompose)

    implementation(Accompanist.insets)
}