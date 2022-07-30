plugins {
	id("com.android.library")
	id("org.jetbrains.kotlin.android")
}

apply<ModuleDefaultPlugin>()

android {
	compileSdk = Android.compileSdk
	namespace = "com.looker.core_ui"

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
	api(project(Modules.coreModel))
	api(project(Modules.coreCommon))

	implementation(Core.core)
	implementation(Palette.palette)

	api(Coil.coil)
	api(Compose.activity)
	api(Compose.runtime)
	api(Compose.ui)
	api(Compose.animation)
	api(Compose.foundation)
	api(Compose.icons)
	api(Compose.material)
}