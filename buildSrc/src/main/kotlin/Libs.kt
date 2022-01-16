object Accompanist {
	private const val accompanistVersion = "0.22.0-rc"
	const val insets = "com.google.accompanist:accompanist-insets:$accompanistVersion"
}

object Classpath {
	private const val gradleVersion = "7.0.4"
	const val gradleClasspath = "com.android.tools.build:gradle:$gradleVersion"
	const val gradleKotlin =
		"org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.kotlinVersion}"
}

object Core {
	private const val coreVersion = "1.7.0"
	const val core = "androidx.core:core-ktx:$coreVersion"
}

object Coil {
	private const val coilVersion = "1.4.0"
	const val coil = "io.coil-kt:coil-compose:$coilVersion"
}

object Compose {
	const val composeCompiler = "1.2.0-alpha01"
	private const val composeVersion = "1.2.0-alpha01"
	private const val activityComposeVersion = "1.4.0"
	private const val navigationVersion = "2.4.0-rc01"

	const val activity = "androidx.activity:activity-compose:$activityComposeVersion"

	const val foundation = "androidx.compose.foundation:foundation:$composeVersion"
	const val icons = "androidx.compose.material:material-icons-extended:$composeVersion"
	const val material = "androidx.compose.material:material:$composeVersion"
	const val tooling = "androidx.compose.ui:ui-tooling:$composeVersion"
	const val ui = "androidx.compose.ui:ui:$composeVersion"
	const val runtime = "androidx.compose.runtime:runtime:$composeVersion"
	const val navigation = "androidx.navigation:navigation-compose:$navigationVersion"
}

object Excludes {
	const val exclude = "/META-INF/{AL2.0,LGPL2.1}"
}

object ExoPlayer {
	private const val exoplayerVersion = "2.16.1"
	const val exoplayer = "com.google.android.exoplayer:exoplayer:$exoplayerVersion"
	const val exoplayerCore = "com.google.android.exoplayer:exoplayer-core:$exoplayerVersion"
	const val exoplayerMediaSession =
		"com.google.android.exoplayer:extension-mediasession:$exoplayerVersion"
}

object Hilt {
	private const val hiltVersion = "2.40.5"
	const val hiltClasspath = "com.google.dagger:hilt-android-gradle-plugin:$hiltVersion"
	const val hiltAndroid = "com.google.dagger:hilt-android:$hiltVersion"
	const val hiltCompiler = "com.google.dagger:hilt-compiler:$hiltVersion"
	const val hiltPlugin = "dagger.hilt.android.plugin"
}

object Lifecycle {
	private const val lifecycleVersion = "2.4.0"
	const val lifecycleViewModelCompose =
		"androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion"
}

object Media3 {
	private const val media3Version = "1.0.0-alpha01"
	const val media3Session = "androidx.media3:media3-session:$media3Version"
	const val media3ExoPlayer = "androidx.media3:media3-exoplayer:$media3Version"
}

object Palette {
	private const val paletteVersion = "1.0.0"
	const val palette = "androidx.palette:palette-ktx:$paletteVersion"
}

object Room {
	private const val roomVersion = "2.4.1"
	const val roomRuntime = "androidx.room:room-runtime:$roomVersion"
	const val roomCompiler = "androidx.room:room-compiler:$roomVersion"
	const val roomKtx = "androidx.room:room-ktx:$roomVersion"
}

object Version {
	const val kotlinVersion = "1.6.10"
}