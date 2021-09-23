apply("$rootDir/android-library.gradle")

val implementation by configurations

dependencies {
    implementation(project(Modules.components))
    implementation(project(Modules.domainMusic))
    implementation(project(Modules.dataMusic))

    implementation(Coil.coil)
    implementation(Compose.runtimeLiveData)
    implementation(Lifecycle.lifecycleViewModelCompose)
}