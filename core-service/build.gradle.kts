plugins {
	id("com.android.library")
	id("org.jetbrains.kotlin.android")
	kotlin("kapt")
	id(Hilt.plugin)
}

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
		compose = false
		buildConfig = false
		aidl = false
		renderScript = false
		resValues = false
		shaders = false
	}
	kotlinOptions {
		jvmTarget = "11"
	}
}

dependencies {
	implementation(project(Modules.coreModel))
	implementation(project(Modules.coreData))
	implementation(project(Modules.coreCommon))
	implementation(project(Modules.coreUi))

	api(ExoPlayer.exoplayerCore)
	api(ExoPlayer.exoplayerUi)
	api(ExoPlayer.exoplayerMediaSession)

	implementation(Lifecycle.lifecycleViewModelCompose)
	implementation(Compose.icons)

	implementation(Hilt.android)
	kapt(Hilt.compiler)
}