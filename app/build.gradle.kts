plugins {
    id("com.android.application")
    kotlin("android")
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
            isShrinkResources = true
        }
        getByName("debug") {
            isDebuggable = true
            applicationIdSuffix = ".debug"
        }
        all {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
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
    implementation(project(Modules.uiSongs))
    implementation(project(Modules.uiAlbums))
    implementation(project(Modules.uiPlayer))
    implementation(project(Modules.components))

    implementation(Core.core)

    implementation(Accompanist.insets)
    implementation(Compose.activity)
    implementation(Compose.icons)
    implementation(Compose.material)
    implementation(Compose.navigation)
    implementation(Compose.ui)
    implementation(Compose.runtimeLiveData)

    debugImplementation(Compose.tooling)
}