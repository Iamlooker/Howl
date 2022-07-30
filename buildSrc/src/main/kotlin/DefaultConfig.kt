import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project

class DefaultConfig : Plugin<Project> {
	override fun apply(project: Project) {
		val androidComponents =
			project.extensions.getByType(ApplicationAndroidComponentsExtension::class.java)
		androidComponents.finalizeDsl { extension ->
			extension.defaultConfig {
				applicationId = Android.appId
				minSdk = Android.minSdk
				targetSdk = Android.compileSdk
				versionCode = Android.versionCode
				versionName = Android.versionName
				vectorDrawables { useSupportLibrary = true }
			}
			extension.buildTypes.getByName("release") {
				it.isMinifyEnabled = true
				it.isShrinkResources = true
				it.proguardFiles(
					extension.getDefaultProguardFile("proguard-android-optimize.txt"),
					project.rootProject.file("proguard-rules.pro")
				)
				it.resValue("string", "app_name", "Howl")
			}
			extension.buildTypes.getByName("debug") {
				it.applicationIdSuffix = ".debug"
				it.resValue("string", "app_name", "Howl-debug")
			}
			extension.buildTypes.create("staging") {
				it.initWith(extension.buildTypes.getByName("release"))
				it.applicationIdSuffix = ".staging"
				it.signingConfig = extension.signingConfigs.getByName("debug")
				it.resValue("string", "app_name", "Howl-staging")
			}
			extension.buildFeatures {
				compose = true
				buildConfig = false
				aidl = false
				renderScript = false
				shaders = false
			}
			extension.compileOptions {
				sourceCompatibility = JavaVersion.VERSION_11
				targetCompatibility = JavaVersion.VERSION_11
			}
			extension.composeOptions {
				kotlinCompilerExtensionVersion = Compose.composeCompiler
			}
			extension.packagingOptions {
				jniLibs {
					excludes += Excludes.jniExclude
				}
				resources {
					excludes += Excludes.listExclude
				}
			}
		}
	}
}

class ModuleStagingPlugin : Plugin<Project> {
	override fun apply(project: Project) {
		val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)
		androidComponents.finalizeDsl { extension ->
			extension.buildTypes.create("staging").let {
				it.initWith(extension.buildTypes.getByName("release"))
				it.proguardFiles("proguard-android-optimize.txt")
			}
		}
	}
}