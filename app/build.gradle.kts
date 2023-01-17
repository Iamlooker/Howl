plugins {
	id("com.android.application")
	id("org.jetbrains.kotlin.android")
	kotlin("kapt")
	id(Hilt.plugin)
}

apply<DefaultConfig>()

android {
	compileSdk = Android.compileSdk
	buildToolsVersion = "33.0.0"
	namespace = Android.appId

	kotlinOptions {
		freeCompilerArgs = freeCompilerArgs + "-opt-in=kotlin.RequiresOptIn"
		jvmTarget = "11"
	}
}

dependencies {
	val composeBom = platform(Compose.composeBom)
	implementation(composeBom)

	implementation(project(Modules.featurePlayer))
	implementation(project(Modules.featureSong))
	implementation(project(Modules.featureAlbum))
	implementation(project(Modules.onBoarding))

	implementation(project(Modules.coreCommon))
	implementation(project(Modules.coreUi))
	implementation(project(Modules.coreService))
	implementation(project(Modules.coreNavigation))
	implementation(project(Modules.newDataMusic))

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