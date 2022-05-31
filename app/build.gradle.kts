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
		}
		getByName("debug") {
			isMinifyEnabled = false
			isShrinkResources = false
			applicationIdSuffix = ".debug"
		}
		all {
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
	}

	kotlinOptions {
		freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
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
	implementation(project(Modules.dataMusic))
	implementation(project(Modules.uiSongs))
	implementation(project(Modules.uiAlbums))
	implementation(project(Modules.featurePlayer))
	implementation(project(Modules.components))

	implementation(Core.core)

	implementation(Coil.coil)

	implementation(Compose.activity)
	implementation(Compose.animation)
	implementation(Compose.runtime)
	implementation(Compose.icons)
	implementation(Compose.material)
	implementation(Compose.foundation)
	implementation(Compose.navigation)
	implementation(Compose.ui)

	implementation(ExoPlayer.exoplayerMediaSession)

	implementation(Hilt.android)
	kapt(Hilt.compiler)

	debugImplementation(Compose.tooling)
}

kapt {
	correctErrorTypes = true
	generateStubs = false
	useBuildCache = true
}