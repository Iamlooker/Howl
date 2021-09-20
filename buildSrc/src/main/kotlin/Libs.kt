object Accompanist {
    private const val accompanistVersion = "0.18.0"
    const val insets = "com.google.accompanist:accompanist-insets:$accompanistVersion"
}

object Classpath {
    private const val gradleVersion = "7.0.2"
    const val gradleClasspath = "com.android.tools.build:gradle:$gradleVersion"
    const val gradleKotlin =
        "org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.kotlinVersion}"
}

object Core {
    private const val coreVersion = "1.6.0"
    const val core = "androidx.core:core-ktx:$coreVersion"
}

object Coil {
    private const val coilVersion = "1.3.2"
    const val coil = "io.coil-kt:coil-compose:$coilVersion"
}

object Compose {
    private const val activityComposeVersion = "1.3.1"
    const val activity = "androidx.activity:activity-compose:$activityComposeVersion"
    const val composeVersion = "1.0.2"

    const val icons = "androidx.compose.material:material-icons-extended:$composeVersion"
    const val material = "androidx.compose.material:material:$composeVersion"
    const val tooling = "androidx.compose.ui:ui-tooling:$composeVersion"
    const val ui = "androidx.compose.ui:ui:$composeVersion"
    const val runtimeLiveData = "androidx.compose.runtime:runtime-livedata:$composeVersion"
    private const val navigationVersion = "2.4.0-alpha07"

    const val navigation = "androidx.navigation:navigation-compose:$navigationVersion"
}

object Excludes {
    const val exclude = "/META-INF/{AL2.0,LGPL2.1}"
}

object ExoPlayer {
    private const val exoplayerVersion = "2.15.0"
    const val exoplayerCore = "com.google.android.exoplayer:exoplayer-core:$exoplayerVersion"
    const val exoplayerMediaSession =
        "com.google.android.exoplayer:extension-mediasession:$exoplayerVersion"
}

object Hilt {
    private const val hiltVersion = "2.38.1"
    const val hiltClasspath = "com.google.dagger:hilt-android-gradle-plugin:$hiltVersion"
    const val hiltAndroid = "com.google.dagger:hilt-android:$hiltVersion"
    const val hiltCompiler = "com.google.dagger:hilt-compiler:$hiltVersion"
    const val hiltPlugin = "dagger.hilt.android.plugin"
}

object Lifecycle {
    private const val lifecycleVersion = "1.0.0-alpha07"
    const val lifecycleViewModelCompose =
        "androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion"
}

object Palette {
    private const val paletteVersion = "1.0.0"
    const val palette = "androidx.palette:palette-ktx:$paletteVersion"
}

object Version {
    const val kotlinVersion = "1.5.21"
}

object Shrink {
    const val shrink = false
}
