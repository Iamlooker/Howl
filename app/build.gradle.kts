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
		targetSdk = Android.compileSdk
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
			applicationIdSuffix = ".debug"
		}
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

	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
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
	implementation(project(Modules.featurePlayer))
	implementation(project(Modules.featureSong))
	implementation(project(Modules.featureAlbum))
	implementation(project(Modules.onBoarding))

	implementation(project(Modules.coreCommon))
	implementation(project(Modules.coreUi))
	implementation(project(Modules.coreService))
	implementation(project(Modules.coreNavigation))
	implementation(project(Modules.newDataMusic))


	implementation(Startup.lib)

	implementation(Core.core)

	implementation(Hilt.android)
	implementation(Hilt.work)
	kapt(Hilt.compiler)
	kapt(Hilt.androidX)
}

kapt {
	correctErrorTypes = true
	generateStubs = false
	useBuildCache = true
}