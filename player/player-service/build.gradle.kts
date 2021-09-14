apply("$rootDir/android-library.gradle")

val implementation by configurations

dependencies {
    implementation(project(Modules.components))
    implementation(project(Modules.constants))
    implementation(project(Modules.dataMusic))
    implementation(ExoPlayer.exoplayerCore)
    implementation(ExoPlayer.exoplayerMediaSession)
}