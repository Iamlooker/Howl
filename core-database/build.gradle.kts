plugins {
	id("com.android.library")
	id("org.jetbrains.kotlin.android")
	kotlin("kapt")
	id("com.google.devtools.ksp") version Kotlin.kspVersion
	id(Hilt.plugin)
}

apply<ModuleDefaultPlugin>()

android {
	compileSdk = Android.compileSdk
	namespace = "com.looker.core_database"

	kotlinOptions {
		jvmTarget = "11"
	}
}

dependencies {
	implementation(project(Modules.coreModel))

	implementation(Room.roomKtx)
	implementation(Room.roomRuntime)
	ksp(Room.roomCompiler)

	implementation(Hilt.android)
	kapt(Hilt.compiler)
}