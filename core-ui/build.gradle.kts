plugins {
	id("com.android.library")
	id("org.jetbrains.kotlin.android")
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
	kotlinOptions {
		jvmTarget = "11"
	}

	buildFeatures {
		compose = true
		buildConfig = false
	}

	composeOptions {
		kotlinCompilerExtensionVersion = Compose.composeCompiler
	}
}

dependencies {
	implementation(project(Modules.coreModel))
	api(project(Modules.components))

	implementation(Core.core)

	api(Coil.coil)
	api(Compose.activity)
	api(Compose.runtime)
	api(Compose.ui)
	api(Compose.animation)
	api(Compose.foundation)
	api(Compose.icons)
	api(Compose.material)

	// Preview
	debugApi("androidx.customview:customview:1.2.0-alpha01")
	debugApi("androidx.customview:customview-poolingcontainer:1.0.0-beta02")
	api(Compose.preview)
	debugApi(Compose.tooling)
}