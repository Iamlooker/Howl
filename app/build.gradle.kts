plugins {
	id("com.android.application")
	id("org.jetbrains.kotlin.android")
	kotlin("kapt")
	id(Hilt.plugin)
}

android {
	compileSdk = Android.compileSdk

	defaultConfig {
		applicationId = Android.appId
		minSdk = Android.minSdk
		targetSdk = Android.targetSdk
		versionCode = Android.versionCode
		versionName = Android.versionName

		vectorDrawables { useSupportLibrary = true }
	}

	buildTypes {
		getByName("release") {
			isMinifyEnabled = true
			isShrinkResources = true
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
		getByName("debug") {
			isMinifyEnabled = false
			isShrinkResources = false
			applicationIdSuffix = ".debug"
		}
	}

	kotlinOptions {
		freeCompilerArgs = freeCompilerArgs + "-opt-in=kotlin.RequiresOptIn"
	}

	buildFeatures {
		compose = true
		buildConfig = false
	}

	composeOptions {
		kotlinCompilerExtensionVersion = Compose.composeCompiler
	}

	packagingOptions {
		resources {
			excludes += Excludes.exclude
		}
	}
}

dependencies {

	implementation(project(Modules.onBoarding))
	implementation(project(Modules.constants))
	implementation(project(Modules.coreModel))
	implementation(project(Modules.coreData))
	implementation(project(Modules.coreUi))
	implementation(project(Modules.coreDatabase))
	implementation(project(Modules.coreCommon))
	implementation(project(Modules.coreNavigation))
	implementation(project(Modules.dataMusic))
	implementation(project(Modules.uiSongs))
	implementation(project(Modules.uiAlbums))
	implementation(project(Modules.featurePlayer))
	implementation(project(Modules.featureSong))
	implementation(project(Modules.components))

	implementation(Startup.lib)

	implementation(Core.core)

	implementation(Compose.activity)
	implementation(Compose.navigation)

	implementation(ExoPlayer.exoplayerMediaSession)

	implementation(Hilt.android)
	implementation(Hilt.work)
	kapt(Hilt.compiler)

	debugImplementation(Compose.tooling)
}

kapt {
	correctErrorTypes = true
	generateStubs = false
	useBuildCache = true
}