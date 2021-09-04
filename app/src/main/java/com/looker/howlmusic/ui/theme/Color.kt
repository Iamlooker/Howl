package com.looker.howlmusic.ui.theme

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color


val Orange = Color(0xFFFF9E80)
val OrangeLight = Color(0xFFFFd0b0)
val Green = Color(0xFF69f0AE)

val HowlColorsDark = darkColors(
    primary = Orange,
    onPrimary = Color.Black,
    primaryVariant = OrangeLight,
    secondary = Green,
    onSecondary = Color.Black
)

val HowlColorsLight = lightColors(
    primary = Orange,
    onPrimary = Color.Black,
    primaryVariant = OrangeLight,
    secondary = Green,
    onSecondary = Color.Black
)