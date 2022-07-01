plugins {
	id("com.android.library")
	id("org.jetbrains.kotlin.android")
}

apply<ModuleStagingPlugin>()

android {
	compileSdk = Android.compileSdk
	namespace = "com.looker.core_ui"

	defaultConfig {
		minSdk = Android.minSdk
		targetSdk = Android.compileSdk

		consumerProguardFiles("consumer-rules.pro")
	}

	buildTypes {
		release {
			isMinifyEnabled = true
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
	}
	kotlinOptions {
		freeCompilerArgs = freeCompilerArgs + "-opt-in=kotlin.RequiresOptIn"
		jvmTarget = "11"
	}
	buildFeatures {
		compose = true
		buildConfig = false
		aidl = false
		renderScript = false
		resValues = false
		shaders = false
	}
	composeOptions {
		kotlinCompilerExtensionVersion = Compose.composeCompiler
	}
}

dependencies {
	api(project(Modules.coreModel))

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