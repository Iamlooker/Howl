plugins {
	id("com.android.library")
	id("org.jetbrains.kotlin.android")
	kotlin("kapt")
}

apply<ModuleDefaultPlugin>()

android {
	compileSdk = Android.compileSdk
	namespace = "com.looker.core.data"

	kotlinOptions {
		jvmTarget = "11"
	}
}

dependencies {
	implementation(project(Modules.coreCommon))
	implementation(project(Modules.coreModel))
	implementation(project(Modules.coreDatabase))
	implementation(project(Modules.dataMusic))

	implementation(Coroutines.android)

	implementation(Hilt.android)
	kapt(Hilt.compiler)
}