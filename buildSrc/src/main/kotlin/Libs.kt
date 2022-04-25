object Accompanist {
    private const val accompanistVersion = "0.23.1"
    const val insets = "com.google.accompanist:accompanist-insets:$accompanistVersion"
}

object Classpath {
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
    const val composeCompiler = "1.1.1"
    private const val composeVersion = "1.1.1"
    private const val activityComposeVersion = "1.4.0"
    private const val navigationVersion = "2.4.2"

    const val activity = "androidx.activity:activity-compose:$activityComposeVersion"

    const val foundation = "androidx.compose.foundation:foundation:$composeVersion"
    const val icons = "androidx.compose.material:material-icons-extended:$composeVersion"
    const val material = "androidx.compose.material:material:$composeVersion"
    const val tooling = "androidx.compose.ui:ui-tooling:$composeVersion"
    const val ui = "androidx.compose.ui:ui:$composeVersion"
    const val runtime = "androidx.compose.runtime:runtime:$composeVersion"
    const val runtimeLivedata = "androidx.compose.runtime:runtime-livedata:$composeVersion"
    const val navigation = "androidx.navigation:navigation-compose:$navigationVersion"
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
	private const val hiltVersion = "2.41"
	const val hiltClasspath = "com.google.dagger:hilt-android-gradle-plugin:$hiltVersion"
	const val hiltAndroid = "com.google.dagger:hilt-android:$hiltVersion"
	const val hiltCompiler = "com.google.dagger:hilt-compiler:$hiltVersion"
	const val hiltPlugin = "dagger.hilt.android.plugin"
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

object Version {
    const val kotlinVersion = "1.6.10"
}