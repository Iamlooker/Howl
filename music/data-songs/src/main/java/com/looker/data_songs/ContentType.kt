package com.looker.data_songs

import android.net.Uri
import android.provider.MediaStore

object ContentType {

    val songsProjections = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.ALBUM_ID,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.ALBUM,
        MediaStore.Audio.Media.DURATION
    )

    val externalUri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

    const val isMusic = MediaStore.Audio.Media.IS_MUSIC + " != 0"
    const val sortOrderSong = MediaStore.Audio.Media.TITLE + " COLLATE NOCASE ASC"

    val Long.path: Uri
        get() = Uri.parse("$externalUri/$this")
}