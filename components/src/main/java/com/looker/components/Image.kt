package com.looker.components

import android.graphics.Bitmap
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.palette.graphics.Palette
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.looker.components.ComponentConstants.DefaultCrossfadeDuration
import com.looker.components.ComponentConstants.tweenAnimation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalCoilApi::class)
@Composable
fun HowlImage(
    modifier: Modifier = Modifier,
    data: Any?,
    imageFillerColor: Color = MaterialTheme.colors.surface,
    shape: CornerBasedShape = MaterialTheme.shapes.medium
) {
    Crossfade(
        targetState = data,
        animationSpec = tweenAnimation(DefaultCrossfadeDuration)
    ) {
        Image(
            modifier = modifier
                .clip(shape)
                .background(imageFillerColor),
            contentScale = ContentScale.FillWidth,
            painter = rememberImagePainter(
                data = it,
                builder = {
                    placeholder(R.drawable.white_background)
                    error(R.drawable.error_image)
                }
            ),
            contentDescription = null
        )
    }
}

suspend fun Bitmap?.getVibrantColor(): Color? = withContext(Dispatchers.IO) {
    this@getVibrantColor?.let {
        Palette.Builder(it)
            .resizeBitmapArea(0)
            .clearFilters()
            .maximumColorCount(8)
            .generate()
    }?.getVibrantColor(0)?.toColor()
}

suspend fun Bitmap?.getDominantColor(): Color? = withContext(Dispatchers.IO) {
    this@getDominantColor?.let {
        Palette.Builder(it)
            .resizeBitmapArea(0)
            .clearFilters()
            .maximumColorCount(8)
            .generate()
    }?.getDominantColor(0)?.toColor()
}