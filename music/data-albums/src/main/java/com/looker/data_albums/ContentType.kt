package com.looker.data_albums

import android.net.Uri
import android.provider.MediaStore

object ContentType {

    val albumsProjections = arrayOf(
        MediaStore.Audio.Media.ALBUM_ID,
        MediaStore.Audio.Media.ALBUM,
        MediaStore.Audio.Media.ARTIST,
    )

    val externalUri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

    const val isMusic = MediaStore.Audio.Media.IS_MUSIC + " != 0"
    const val sortOrderAlbum = MediaStore.Audio.Media.ALBUM + " COLLATE NOCASE ASC"
}