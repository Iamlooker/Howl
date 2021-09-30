package com.looker.player_service.service.extensions

import android.net.Uri
import java.net.URLEncoder
import java.nio.charset.Charset
import java.util.*

fun String?.containsCaseInsensitive(other: String?) =
    if (this != null && other != null) {
        lowercase(Locale.getDefault()).contains(other.lowercase(Locale.getDefault()))
    } else {
        this == other
    }

inline val String?.urlEncoded: String
    get() = if (Charset.isSupported("UTF-8")) {
        URLEncoder.encode(this ?: "", "UTF-8")
    } else {
        // If UTF-8 is not supported, use the default charset.
        @Suppress("deprecation")
        URLEncoder.encode(this ?: "")
    }

fun String?.toUri(): Uri = this?.let { Uri.parse(it) } ?: Uri.EMPTY
