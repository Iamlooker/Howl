plugins {
	id("com.android.library")
	id("org.jetbrains.kotlin.android")
	kotlin("kapt")
	id(Hilt.plugin)
}

apply<ModuleDefaultPlugin>()

android {
	compileSdk = Android.compileSdk
	namespace = "com.looker.feature_album"

	kotlinOptions {
		freeCompilerArgs = freeCompilerArgs + "-opt-in=kotlin.RequiresOptIn"
		jvmTarget = "11"
	}
	buildFeatures {
		compose = true
	}
	composeOptions {
		kotlinCompilerExtensionVersion = Compose.composeCompiler
	}
}

dependencies {
	implementation(project(Modules.coreCommon))
	implementation(project(Modules.coreData))
	implementation(project(Modules.coreNavigation))
	implementation(project(Modules.coreUi))
	implementation(project(Modules.coreService))

	implementation(Lifecycle.viewModelCompose)

	implementation(Hilt.android)
	kapt(Hilt.compiler)
}