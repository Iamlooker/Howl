package com.looker.core_service.utils

import android.support.v4.media.session.PlaybackStateCompat
import androidx.annotation.IntDef

@Target(AnnotationTarget.EXPRESSION)
@IntDef(
	SHUFFLE_MODE_ALL,
	SHUFFLE_MODE_NONE,
	SHUFFLE_MODE_INVALID,
	SHUFFLE_MODE_GROUP
)
@Retention(AnnotationRetention.SOURCE)
annotation class ShuffleMode

const val SHUFFLE_MODE_INVALID = PlaybackStateCompat.SHUFFLE_MODE_INVALID
const val SHUFFLE_MODE_NONE = PlaybackStateCompat.SHUFFLE_MODE_NONE
const val SHUFFLE_MODE_ALL = PlaybackStateCompat.SHUFFLE_MODE_ALL
const val SHUFFLE_MODE_GROUP = PlaybackStateCompat.SHUFFLE_MODE_GROUP