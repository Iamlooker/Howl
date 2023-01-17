object Core {
	private const val coreVersion = "1.9.0"
	const val core = "androidx.core:core-ktx:$coreVersion"
}

object Coil {
	private const val coilVersion = "2.2.2"
	const val coil = "io.coil-kt:coil-compose:$coilVersion"
}

object Compose {
	const val composeBom = "androidx.compose:compose-bom:2023.01.00"
	const val composeCompiler = "1.3.2"
	private const val composeVersion = "1.3.3"
	private const val activityComposeVersion = "1.6.1"
	private const val navigationVersion = "2.5.3"
	private const val material3Version = "1.0.1"

	const val activity = "androidx.activity:activity-compose:$activityComposeVersion"

	const val animation = "androidx.compose.animation:animation"
	const val foundation = "androidx.compose.foundation:foundation"
	const val icons = "androidx.compose.material:material-icons-extended"
	const val material = "androidx.compose.material:material"
	const val material3 = "androidx.compose.material3:material3:$material3Version"
	const val tooling = "androidx.compose.ui:ui-tooling"
	const val preview = "androidx.compose.ui:ui-tooling-preview"
	const val ui = "androidx.compose.ui:ui"
	const val runtime = "androidx.compose.runtime:runtime"
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
	private const val exoplayerVersion = "2.18.1"
	const val exoplayerCore = "com.google.android.exoplayer:exoplayer-core:$exoplayerVersion"
	const val exoplayerUi = "com.google.android.exoplayer:exoplayer-ui:$exoplayerVersion"
	const val exoplayerMediaSession =
		"com.google.android.exoplayer:extension-mediasession:$exoplayerVersion"
}

object Hilt {
	private const val hiltVersion = "2.44.2"
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
	private const val roomVersion = "2.5.0"
	const val roomRuntime = "androidx.room:room-runtime:$roomVersion"
	const val roomCompiler = "androidx.room:room-compiler:$roomVersion"
	const val roomKtx = "androidx.room:room-ktx:$roomVersion"
}

object Startup {
	private const val startupVersion = "1.1.1"
	const val lib = "androidx.startup:startup-runtime:$startupVersion"
}

object Kotlin {
	const val version = "1.7.20"
	const val kspVersion = "1.7.20-1.0.8"
}

object WorkManager {
	private const val workVersion = "2.7.1"
	const val ktx = "androidx.work:work-runtime-ktx:$workVersion"
}