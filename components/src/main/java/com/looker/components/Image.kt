package com.looker.components

import android.graphics.Bitmap
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

@OptIn(ExperimentalCoilApi::class)
@Composable
fun HowlImage(
    modifier: Modifier = Modifier,
    data: Any?,
    imageFillerColor: Color = MaterialTheme.colors.surface,
    shape: CornerBasedShape = MaterialTheme.shapes.medium
) {
    Image(
        modifier = modifier
            .clip(shape)
            .background(imageFillerColor),
        contentScale = ContentScale.FillWidth,
        painter = rememberImagePainter(
            data = data,
            builder = {
                crossfade(true)
                placeholder(R.drawable.white_background)
                error(R.drawable.error_image)
            }
        ),
        contentDescription = null
    )
}

fun Bitmap?.getVibrantColor(): Color? = this?.let {
    Palette.Builder(it)
        .resizeBitmapArea(0)
        .clearFilters()
        .maximumColorCount(8)
        .generate()
}?.getVibrantColor(0)?.toColor()

fun Bitmap?.getDominantColor(): Color? = this?.let {
    Palette.Builder(it)
        .resizeBitmapArea(0)
        .clearFilters()
        .maximumColorCount(8)
        .generate()
}?.getDominantColor(0)?.toColor()