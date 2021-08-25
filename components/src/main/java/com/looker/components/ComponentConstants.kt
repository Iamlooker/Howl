package com.looker.components

import android.content.Context
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object ComponentConstants {
    fun Context.itemSize(height: Boolean, count: Int, padding: Dp = 0.dp): Dp {
        val screenDensity = this.resources.displayMetrics.density
        val screenHeight = this.resources.displayMetrics.heightPixels
        val screenWidth = this.resources.displayMetrics.widthPixels
        val itemSizeInPx = if (height) {
            screenHeight / count
        } else {
            screenWidth / count
        }
        val itemSize = itemSizeInPx.dp / screenDensity

        return itemSize - padding
    }

}