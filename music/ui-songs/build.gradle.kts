apply("$rootDir/android-library.gradle")

val implementation by configurations

dependencies {
    implementation(project(Modules.components))
    implementation(project(Modules.dataMusic))
    implementation(project(Modules.playerService))

    implementation(Compose.runtimeLiveData)
    implementation(Lifecycle.lifecycleViewModelCompose)
}