plugins {
	id("com.android.library")
	id("org.jetbrains.kotlin.android")
	kotlin("kapt")
	id(Hilt.plugin)
}

apply<ModuleDefaultPlugin>()

android {
	compileSdk = Android.compileSdk
	namespace = "com.looker.core.service"

	kotlinOptions {
		jvmTarget = "11"
	}
}

dependencies {
	implementation(project(Modules.coreModel))
	implementation(project(Modules.coreData))
	implementation(project(Modules.coreCommon))
	implementation(project(Modules.coreUi))

	api(ExoPlayer.exoplayerCore)
	api(ExoPlayer.exoplayerUi)
	api(ExoPlayer.exoplayerMediaSession)

	implementation(Lifecycle.viewModelCompose)

	implementation(Hilt.android)
	kapt(Hilt.compiler)
}