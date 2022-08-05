object Core {
	private const val coreVersion = "1.8.0"
	const val core = "androidx.core:core-ktx:$coreVersion"
}

object Coil {
	private const val coilVersion = "2.1.0"
	const val coil = "io.coil-kt:coil-compose:$coilVersion"
}

object Compose {
	const val composeCompiler = "1.3.0-rc02"
	private const val composeVersion = "1.3.0-alpha02"
	private const val activityComposeVersion = "1.5.1"
	private const val navigationVersion = "2.5.1"
	private const val material3Version = "1.0.0-alpha15"

	const val activity = "androidx.activity:activity-compose:$activityComposeVersion"

	const val animation = "androidx.compose.animation:animation:$composeVersion"
	const val foundation = "androidx.compose.foundation:foundation:$composeVersion"
	const val icons = "androidx.compose.material:material-icons-extended:$composeVersion"
	const val material = "androidx.compose.material:material:$composeVersion"
	const val material3 = "androidx.compose.material3:material3:$material3Version"
	const val tooling = "androidx.compose.ui:ui-tooling:$composeVersion"
	const val preview = "androidx.compose.ui:ui-tooling-preview:$composeVersion"
	const val ui = "androidx.compose.ui:ui:$composeVersion"
	const val runtime = "androidx.compose.runtime:runtime:$composeVersion"
	const val navigation = "androidx.navigation:navigation-compose:$navigationVersion"
}

object Coroutines {
	private const val coroutinesVersion = "1.6.4"
	const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"
	const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion"
}

object Excludes {
	const val jniExclude = "/okhttp3/internal/publicsuffix/*"
	val listExclude: List<String> = listOf(
		"/DebugProbesKt.bin",
		"/kotlin/**.kotlin_builtins",
		"/kotlin/**.kotlin_metadata",
		"/META-INF/**.kotlin_module",
		"/META-INF/**.pro",
		"/META-INF/**.version",
		"/okhttp3/internal/publicsuffix/*"
	)
}

object ExoPlayer {
	private const val exoplayerVersion = "2.18.0"
	const val exoplayerCore = "com.google.android.exoplayer:exoplayer-core:$exoplayerVersion"
	const val exoplayerUi = "com.google.android.exoplayer:exoplayer-ui:$exoplayerVersion"
	const val exoplayerMediaSession =
		"com.google.android.exoplayer:extension-mediasession:$exoplayerVersion"
}

object Hilt {
	private const val hiltVersion = "2.43"
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
	private const val version = "2.5.1"
	const val viewModelCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:$version"
}

object Media {
	private const val version = "1.0.0-beta02"
	const val exoplayer = "androidx.media3:media3-exoplayer:$version"
	const val session = "androidx.media3:media3-session:$version"
}

object Palette {
	private const val paletteVersion = "1.0.0"
	const val palette = "androidx.palette:palette-ktx:$paletteVersion"
}

object Room {
	private const val roomVersion = "2.4.3"
	const val roomRuntime = "androidx.room:room-runtime:$roomVersion"
	const val roomCompiler = "androidx.room:room-compiler:$roomVersion"
	const val roomKtx = "androidx.room:room-ktx:$roomVersion"
}

object Startup {
	private const val startupVersion = "1.1.1"
	const val lib = "androidx.startup:startup-runtime:$startupVersion"
}

object Kotlin {
	const val version = "1.7.10"
	const val kspVersion = "1.7.10-1.0.6"
}

object WorkManager {
	private const val workVersion = "2.7.1"
	const val ktx = "androidx.work:work-runtime-ktx:$workVersion"
}