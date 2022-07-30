plugins {
	id("com.android.library")
	id("org.jetbrains.kotlin.android")
}

apply<ModuleDefaultPlugin>()

android {
	compileSdk = Android.compileSdk
	namespace = "com.looker.onboarding"

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
	implementation(project(Modules.coreUi))

	implementation(Core.core)
	implementation(Lifecycle.viewModelCompose)
}