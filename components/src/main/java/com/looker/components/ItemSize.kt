package com.looker.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

fun Modifier.itemSize(
    calculateHeight: Boolean, count: Float, padding: Dp = 0.dp
): Modifier = composed {

    val context = LocalContext.current

    val contentSize = remember {
        mutableStateOf(10.dp)
    }

    val screenDensity = context.resources.displayMetrics.density
    val itemSizeInPx = remember(count, screenDensity) {
        if (calculateHeight) {
            val screenHeight = context.resources.displayMetrics.heightPixels
            screenHeight / count
        } else {
            val screenWidth = context.resources.displayMetrics.widthPixels
            screenWidth / count
        }
    }
    val itemSize = remember(count, calculateHeight, screenDensity, itemSizeInPx) {
        itemSizeInPx.dp / screenDensity
    }

    LaunchedEffect(itemSize) {
        launch {
            contentSize.value = itemSize - padding
        }
    }

    this.then(
        Modifier
            .animateContentSize(
                animationSpec = spring(
                    stiffness = Spring.StiffnessLow
                )
            )
            .size(contentSize.value)
    )
}