plugins {
	id("com.android.library")
	id("org.jetbrains.kotlin.android")
}

apply<ModuleDefaultPlugin>()

android {
	compileSdk = Android.compileSdk
	namespace = "com.looker.music_data"

	kotlinOptions {
		jvmTarget = "11"
	}
}

dependencies {
	implementation(project(Modules.coreModel))
	implementation(Coroutines.core)
}