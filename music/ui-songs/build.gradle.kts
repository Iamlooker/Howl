apply("$rootDir/android-library.gradle")

val implementation by configurations

dependencies {
    implementation(project(Modules.components))
    implementation(project(Modules.domainMusic))
    implementation(project(Modules.dataMusic))
    implementation(project(Modules.playerService))

    implementation(Coil.coil)
    implementation(Compose.runtimeLiveData)
    implementation(Lifecycle.lifecycleViewModelCompose)
}