object Classpath {
	const val gradleKotlin =
		"org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.kotlinVersion}"
}

object Core {
	private const val coreVersion = "1.8.0"
	const val core = "androidx.core:core-ktx:$coreVersion"
}

object Coil {
	private const val coilVersion = "2.1.0"
	const val coil = "io.coil-kt:coil-compose:$coilVersion"
}

object Compose {
	const val composeCompiler = "1.2.0-beta03"
	private const val composeVersion = "1.2.0-beta03"
	private const val activityComposeVersion = "1.5.0-rc01"
	private const val navigationVersion = "2.5.0-rc01"

	const val activity = "androidx.activity:activity-compose:$activityComposeVersion"

	const val animation = "androidx.compose.animation:animation:$composeVersion"
	const val foundation = "androidx.compose.foundation:foundation:$composeVersion"
	const val icons = "androidx.compose.material:material-icons-extended:$composeVersion"
	const val material = "androidx.compose.material:material:$composeVersion"
	const val tooling = "androidx.compose.ui:ui-tooling:$composeVersion"
	const val preview = "androidx.compose.ui:ui-tooling-preview:$composeVersion"
	const val ui = "androidx.compose.ui:ui:$composeVersion"
	const val runtime = "androidx.compose.runtime:runtime:$composeVersion"
	const val navigation = "androidx.navigation:navigation-compose:$navigationVersion"
}

object Coroutines {
	private const val coroutinesVersion = "1.6.2"
	const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"
	const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion"
}

object Excludes {
	const val exclude = "/META-INF/{AL2.0,LGPL2.1}"
}

object ExoPlayer {
	private const val exoplayerVersion = "2.17.1"
	const val exoplayer = "com.google.android.exoplayer:exoplayer:$exoplayerVersion"
	const val exoplayerMediaSession =
		"com.google.android.exoplayer:extension-mediasession:$exoplayerVersion"
}

object Hilt {
	private const val hiltVersion = "2.42"
	const val classpath = "com.google.dagger:hilt-android-gradle-plugin:$hiltVersion"
	const val android = "com.google.dagger:hilt-android:$hiltVersion"
	const val compiler = "com.google.dagger:hilt-compiler:$hiltVersion"
	const val plugin = "dagger.hilt.android.plugin"

	private const val androidXHilt = "1.0.0"
	const val work = "androidx.hilt:hilt-work:$androidXHilt"
	const val androidX = "androidx.hilt:hilt-compiler:$androidXHilt"
	const val navigation = "androidx.hilt:hilt-navigation-compose:$androidXHilt"
}

object Lifecycle {
	private const val lifecycleVersion = "2.4.1"
	const val lifecycleViewModelCompose =
		"androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion"
}

object Palette {
	private const val paletteVersion = "1.0.0"
	const val palette = "androidx.palette:palette-ktx:$paletteVersion"
}

object Room {
	private const val roomVersion = "2.4.2"
	const val roomRuntime = "androidx.room:room-runtime:$roomVersion"
	const val roomCompiler = "androidx.room:room-compiler:$roomVersion"
	const val roomKtx = "androidx.room:room-ktx:$roomVersion"
}

object Startup {
	private const val startupVersion = "1.1.1"
	const val lib = "androidx.startup:startup-runtime:$startupVersion"
}

object Version {
	const val kotlinVersion = "1.6.21"
}

object WorkManager {
	private const val workVersion = "2.7.1"
	const val ktx = "androidx.work:work-runtime-ktx:$workVersion"
}