plugins {
	id("com.android.library")
	id("org.jetbrains.kotlin.android")
	kotlin("kapt")
	id(Hilt.plugin)
}

apply<ModuleDefaultPlugin>()

android {
	compileSdk = Android.compileSdk
	namespace = "com.looker.feature.player"

	buildFeatures {
		compose = true
	}
	composeOptions {
		kotlinCompilerExtensionVersion = Compose.composeCompiler
	}
	kotlinOptions {
		freeCompilerArgs = freeCompilerArgs + "-opt-in=kotlin.RequiresOptIn"
		jvmTarget = "11"
	}
}

dependencies {
	implementation(project(Modules.coreCommon))
	implementation(project(Modules.coreUi))
	implementation(project(Modules.coreService))

	implementation(Lifecycle.viewModelCompose)

	kapt(Hilt.compiler)
	implementation(Hilt.android)
	implementation(Hilt.navigation)
}