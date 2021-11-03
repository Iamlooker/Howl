package com.looker.howlmusic.ui.theme

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

val Orange50 = Color(0xFFFDF3F0)
val Orange100 = Color(0xFFFFBEAA)
val Orange200 = Color(0xFFFFAB91)
val Orange300 = Color(0xFFFF9B7C)
val Orange900 = Color(0xFF221C1C)
val Green100 = Color(0xFFC8E6C9)
val Green200 = Color(0xFFA5D6A7)

val HowlColorsLight = lightColors(
    primary = Orange300,
    primaryVariant = Orange200,
    onPrimary = Color.Black,
    secondary = Green200,
    secondaryVariant = Green100,
    onSecondary = Color.Black,
    surface = Orange50
)

val HowlColorsDark = darkColors(
    primary = Orange200,
    primaryVariant = Orange100,
    onPrimary = Color.Black,
    secondary = Green200,
    secondaryVariant = Green100,
    onSecondary = Color.Black,
    surface = Orange900
)