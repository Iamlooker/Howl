plugins {
	id("com.android.library")
	id("org.jetbrains.kotlin.android")
	kotlin("kapt")
	id(Hilt.plugin)
}

android {
	compileSdk = Android.targetSdk

	defaultConfig {
		minSdk = Android.minSdk
		targetSdk = Android.targetSdk

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
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
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
		jvmTarget = "1.8"
	}
}

dependencies {
	implementation(project(Modules.coreModel))
	implementation(project(Modules.dataMusic))
	implementation(project(Modules.constants))
	implementation(project(Modules.components))

	implementation(Core.core)

	implementation(Compose.icons)
	implementation(Compose.foundation)
	implementation(Compose.runtime)
	implementation(Compose.material)
	implementation(Compose.ui)
	debugImplementation(Compose.tooling)

	implementation(ExoPlayer.exoplayer)
	implementation(ExoPlayer.exoplayerMediaSession)

	kapt(Hilt.compiler)
	implementation(Hilt.android)
}