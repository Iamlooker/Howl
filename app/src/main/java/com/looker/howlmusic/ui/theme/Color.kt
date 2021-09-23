package com.looker.howlmusic.ui.theme

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

val Orange100 = Color(0xFFFDF3F0)
val Orange200 = Color(0xFFFFAB91)
val Orange300 = Color(0xFFFF8A65)
val Orange900 = Color(0xFF221C1C)
val Green100 = Color(0xFFC8E6C9)
val Green200 = Color(0xFFA5D6A7)
val Green300 = Color(0xFF81C784)

val HowlColorsLight = lightColors(
    primary = Orange300,
    primaryVariant = Orange200,
    secondary = Green300,
    secondaryVariant = Green200,
    onSecondary = Color.Black,
    surface = Orange100
)

val HowlColorsDark = darkColors(
    primary = Orange200,
    primaryVariant = Orange200,
    secondary = Green200,
    secondaryVariant = Green100,
    onSecondary = Color.Black,
    surface = Orange900
)