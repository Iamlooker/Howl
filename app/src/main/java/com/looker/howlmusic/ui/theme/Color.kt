package com.looker.howlmusic.ui.theme

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color


val Orange = Color(0xFFFF9E80)
val OrangeLight = Color(0xFFFFd0b0)
val Green = Color(0xFF69f0AE)
val GreenLight = Color(0xFF9fffE0)

val HowlColorsDark = darkColors(
    primary = Orange,
    onPrimary = Color.Black,
    primaryVariant = Orange,
    secondary = Green,
    onSecondary = Color.Black
)

val HowlColorsLight = lightColors(
    primary = OrangeLight,
    onPrimary = Color.Black,
    primaryVariant = OrangeLight,
    secondary = GreenLight,
    onSecondary = Color.Black
)