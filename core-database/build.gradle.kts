plugins {
	id("com.android.library")
	id("org.jetbrains.kotlin.android")
	kotlin("kapt")
	id("com.google.devtools.ksp") version ("1.7.0-1.0.6")
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

	implementation(Room.roomKtx)
	implementation(Room.roomRuntime)
	ksp(Room.roomCompiler)

	implementation(Hilt.android)
	kapt(Hilt.compiler)
}