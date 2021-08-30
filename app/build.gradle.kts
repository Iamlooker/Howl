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
            isMinifyEnabled = true
            isShrinkResources = true
        }
        getByName("debug") {
            applicationIdSuffix = ".debug"
        }
        all {
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
        kotlinCompilerExtensionVersion = Libs.Compose.composeVersion
    }
    packagingOptions {
        resources {
            excludes += Libs.Excludes.exclude
        }
    }
}

dependencies {

    implementation(project(Modules.constants))
    implementation(project(Modules.onBoarding))
    implementation(project(Modules.uiSongs))
    implementation(project(Modules.uiAlbums))
    implementation(project(Modules.dataSongs))
    implementation(project(Modules.dataAlbums))

    implementation(Libs.Core.core)

    implementation(Libs.Accompanist.insets)
    implementation(Libs.Compose.activity)
    implementation(Libs.Compose.icons)
    implementation(Libs.Compose.material)
    implementation(Libs.Compose.navigation)
    implementation(Libs.Compose.tooling)
    implementation(Libs.Compose.ui)

    debugImplementation(Libs.Compose.preview)
}