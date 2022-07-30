plugins {
	id("com.android.library")
	id("org.jetbrains.kotlin.android")
	kotlin("kapt")
	id(Hilt.plugin)
}

apply<ModuleDefaultPlugin>()

android {
	compileSdk = Android.compileSdk
	namespace = "com.looker.data_music"

	kotlinOptions {
		jvmTarget = "11"
	}
}

dependencies {
	implementation(project(Modules.coreData))

	implementation(WorkManager.ktx)
	implementation(Startup.lib)

	implementation(Hilt.work)
	implementation(Hilt.android)
	kapt(Hilt.compiler)
	kapt(Hilt.androidX)
}