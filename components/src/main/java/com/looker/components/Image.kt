package com.looker.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.Coil
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.size.Scale
import com.looker.components.ComponentConstants.DefaultCrossfadeDuration
import com.looker.components.ComponentConstants.tweenAnimation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun String.bitmap(context: Context): Bitmap {
    val r = ImageRequest.Builder(context)
        .data(this)
        .size(128)
        .scale(Scale.FILL)
        .allowHardware(false)
        .build()

    val bitmap = when (val result = Coil.execute(r)) {
        is SuccessResult -> result.drawable.toBitmap()
        else -> BitmapFactory.decodeResource(context.resources, R.drawable.error_image)
    }
    return bitmap
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun HowlImage(
    modifier: Modifier = Modifier,
    data: Any?,
    imageFillerColor: Color = MaterialTheme.colors.surface,
    shape: CornerBasedShape = MaterialTheme.shapes.medium,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit = {}
) {
    Crossfade(
        targetState = data,
        animationSpec = tweenAnimation(DefaultCrossfadeDuration)
    ) {
        Image(
            modifier = modifier
                .clip(shape)
                .background(imageFillerColor)
                .clickable(
                    onClick = onClick,
                    indication = null,
                    interactionSource = interactionSource
                ),
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