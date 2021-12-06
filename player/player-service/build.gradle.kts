apply("$rootDir/android-library.gradle")

val implementation by configurations
val api by configurations
val kapt by configurations

dependencies {
    implementation(project(Modules.constants))
    implementation(project(Modules.components))
    implementation(project(Modules.domainMusic))

    implementation(Coil.coil)
    implementation(ExoPlayer.exoplayer)
    implementation(ExoPlayer.exoplayerMediaSession)

    api(Glide.glide)
    kapt(Glide.glideCompiler)
}