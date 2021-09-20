plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id(Hilt.hiltPlugin)
}

android {
    compileSdk = Android.compileSdk

    defaultConfig {
        applicationId = Android.appId
        minSdk = Android.minSdk
        targetSdk = Android.targetSdk
        versionCode = Android.versionCode
        versionName = Android.versionName

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            isShrinkResources = Shrink.shrink
        }
        getByName("debug") {
            isDebuggable = true
            applicationIdSuffix = ".debug"
        }
        all {
            isMinifyEnabled = Shrink.shrink
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
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
        kotlinCompilerExtensionVersion = Compose.composeVersion
    }

    packagingOptions {
        resources {
            excludes += Excludes.exclude
        }
    }
}

dependencies {

    implementation(project(Modules.constants))
    implementation(project(Modules.onBoarding))
    implementation(project(Modules.domainMusic))
    implementation(project(Modules.uiSongs))
    implementation(project(Modules.uiAlbums))
    implementation(project(Modules.uiPlayer))
    implementation(project(Modules.playerService))
    implementation(project(Modules.components))

    implementation(Core.core)

    implementation(Accompanist.insets)
    implementation(Compose.activity)
    implementation(Compose.icons)
    implementation(Compose.material)
    implementation(Compose.navigation)
    implementation(Compose.ui)
    implementation(Compose.runtimeLiveData)

    implementation(ExoPlayer.exoplayerCore)

    implementation(Hilt.hiltAndroid)
    kapt(Hilt.hiltCompiler)

    debugImplementation(Compose.tooling)
}

kapt {
    correctErrorTypes = true
    generateStubs = false
}