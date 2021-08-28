package com.looker.data_songs.data

import android.net.Uri
import androidx.core.net.toUri
import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromUri(uri: Uri): String = uri.toString()

    @TypeConverter
    fun toUri(string: String): Uri = string.toUri()
}