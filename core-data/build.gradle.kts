plugins {
	id("com.android.library")
	id("org.jetbrains.kotlin.android")
	kotlin("kapt")
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
	implementation(project(Modules.coreCommon))
	implementation(project(Modules.coreDatabase))
	implementation(project(Modules.dataMusic))

	implementation(Coroutines.android)

	implementation(Hilt.android)
	implementation(Hilt.work)
	kapt(Hilt.compiler)
}