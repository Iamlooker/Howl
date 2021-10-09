package com.looker.components.localComposers

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf

@Immutable
data class Images(@DrawableRes val appIcon: Int)

val LocalImages = staticCompositionLocalOf<Images> {
    error("No Local Image")
}