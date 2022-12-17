package com.looker.core.ui.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.size.Scale
import com.looker.core.ui.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun String.bitmap(context: Context): Bitmap = withContext(Dispatchers.IO) {
	val imageRequest = ImageRequest.Builder(context)
		.data(this@bitmap)
		.size(128)
		.scale(Scale.FILL)
		.allowHardware(false)
		.build()

	when (val result = imageRequest.context.imageLoader.execute(imageRequest)) {
		is SuccessResult -> result.drawable.toBitmap()
		is ErrorResult -> R.drawable.error_image.bitmap(context)
	}
}

suspend fun Int.bitmap(context: Context): Bitmap = withContext(Dispatchers.IO) {
	val imageRequest = ImageRequest.Builder(context)
		.data(this@bitmap)
		.size(128)
		.scale(Scale.FILL)
		.allowHardware(false)
		.build()

	when (val result = imageRequest.context.imageLoader.execute(imageRequest)) {
		is SuccessResult -> result.drawable.toBitmap()
		is ErrorResult -> BitmapFactory.decodeResource(context.resources, R.drawable.error_image)
	}
}

@Composable
fun HowlImage(
	modifier: Modifier = Modifier,
	data: () -> String?,
	contentScale: ContentScale = ContentScale.Crop,
	shape: CornerBasedShape = MaterialTheme.shapes.medium
) {
	Box(modifier) {
		AsyncImage(
			model = data(),
			contentDescription = null,
			contentScale = contentScale,
			modifier = Modifier
				.matchParentSize()
				.clip(shape)
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