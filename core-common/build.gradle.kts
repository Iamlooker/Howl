plugins {
	id("com.android.library")
	id("org.jetbrains.kotlin.android")
}

apply<ModuleDefaultPlugin>()

android {
	compileSdk = Android.compileSdk
	namespace = "com.looker.core_common"

	kotlinOptions {
		jvmTarget = "11"
	}
}

dependencies {
	implementation(Coroutines.core)
	implementation(Coroutines.android)
}