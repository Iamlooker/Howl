plugins {
	id("com.android.library")
	id("org.jetbrains.kotlin.android")
	kotlin("kapt")
	id(Hilt.plugin)
}

apply<ModuleStagingPlugin>()

android {
	compileSdk = Android.compileSdk

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
	kotlinOptions {
		freeCompilerArgs = freeCompilerArgs + "-opt-in=kotlin.RequiresOptIn"
		jvmTarget = "11"
	}
}

dependencies {
	implementation(project(Modules.coreCommon))
	implementation(project(Modules.coreUi))
	implementation(project(Modules.coreService))

	implementation(Lifecycle.lifecycleViewModelCompose)

	kapt(Hilt.compiler)
	implementation(Hilt.android)
	implementation(Hilt.navigation)
}