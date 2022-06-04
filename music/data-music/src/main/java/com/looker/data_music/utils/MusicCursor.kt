package com.looker.data_music.utils

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log

object MusicCursor {

	val externalUri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

	private const val isMusic = MediaStore.Audio.Media.IS_MUSIC + " != 0"

	fun generateCursor(
		context: Context,
		projection: Array<String>,
		sortOrder: String
	): Cursor? =
		try {
			context.contentResolver.query(
				externalUri,
				projection,
				isMusic,
				null,
				sortOrder
			)
		} catch (e: NullPointerException) {
			Log.e("CursorError", "Cant create cursor")
			null
		}
}