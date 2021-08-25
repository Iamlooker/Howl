object Libs {

    object Classpath {
        private const val gradleVersion = "7.0.1"
        const val gradleClasspath = "com.android.tools.build:gradle:$gradleVersion"
        const val gradleKotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.kotlinVersion}"
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
        const val composeVersion = "1.0.1"

        const val icons = "androidx.compose.material:material-icons-extended:$composeVersion"
        const val material = "androidx.compose.material:material:$composeVersion"
        const val preview = "androidx.compose.ui:ui-tooling-preview:$composeVersion"
        const val tooling = "androidx.compose.ui:ui-tooling:$composeVersion"
        const val ui = "androidx.compose.ui:ui:$composeVersion"
        private const val navigationVersion = "2.4.0-alpha07"

        const val navigation = "androidx.navigation:navigation-compose:$navigationVersion"
    }

    object Coroutines {
        private const val coroutinesVersion = "1.5.1"
        const val coroutinesCore =
            "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"
        const val coroutinesAndroid =
            "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion"
    }

    object Palette {
        private const val paletteVersion = "1.0.0"
        const val palette = "androidx.palette:palette-ktx:$paletteVersion"
    }
}

object Version {
    const val kotlinVersion = "1.5.21"
}