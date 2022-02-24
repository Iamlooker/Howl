package com.looker.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.Coil
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.size.Scale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun toBitmap(context: Context, data: String, onLoaded: (Bitmap) -> Unit): Bitmap? {
	val loader = ImageLoader(context)
	val req = ImageRequest.Builder(context)
		.data(data)
		.target { result ->
			onLoaded((result as BitmapDrawable).bitmap)
		}
		.build()
	loader.enqueue(req)
	return null
}

suspend fun String.bitmap(context: Context): Bitmap {
	val r = ImageRequest.Builder(context)
		.data(this)
		.size(128)
		.scale(Scale.FILL)
		.allowHardware(false)
		.build()

	return when (val result = Coil.execute(r)) {
		is SuccessResult -> result.drawable.toBitmap()
		is ErrorResult -> R.drawable.error_image.bitmap(context)
	}
}

suspend fun Int.bitmap(context: Context): Bitmap {
	val r = ImageRequest.Builder(context)
		.data(this)
		.size(128)
		.scale(Scale.FILL)
		.allowHardware(false)
		.build()

	return when (val result = Coil.execute(r)) {
		is SuccessResult -> result.drawable.toBitmap()
		is ErrorResult -> BitmapFactory.decodeResource(context.resources, R.drawable.error_image)
	}
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun HowlImage(
	modifier: Modifier = Modifier,
	data: String?,
	contentScale: ContentScale = ContentScale.Crop,
	backgroundColor: Color = MaterialTheme.colors.surface,
	shape: CornerBasedShape = MaterialTheme.shapes.medium
) {
	Box(modifier) {
		val painter = rememberImagePainter(data = data)

		Image(
			painter = painter,
			contentDescription = "This is Album Art",
			contentScale = contentScale,
			modifier = Modifier
				.matchParentSize()
				.clip(shape)
		)

		if (painter.state is ImagePainter.State.Loading) {
			Spacer(
				modifier = Modifier
					.matchParentSize()
					.background(backgroundColor)
			)
		}
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