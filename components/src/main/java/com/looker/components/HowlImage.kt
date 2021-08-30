package com.looker.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter

@OptIn(ExperimentalCoilApi::class)
@Composable
fun HowlImage(
    modifier: Modifier = Modifier,
    data: Uri?,
    size: Int = 600,
    shape: CornerBasedShape = MaterialTheme.shapes.medium,
) {
    Image(
        modifier = modifier.clip(shape),
        painter = rememberImagePainter(
            data = data,
            builder = {
                crossfade(true)
                placeholder(R.drawable.white_background)
                error(R.drawable.error_image)
                size(size)
            }
        ),
        contentDescription = null
    )
}