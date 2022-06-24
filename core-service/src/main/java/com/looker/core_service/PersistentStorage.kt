package com.looker.core_service

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
import android.support.v4.media.MediaDescriptionCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PersistentStorage private constructor(val context: Context) {

	/**
	 * Store any data which must persist between restarts, such as the most recently played song.
	 */
	private var preferences: SharedPreferences = context.getSharedPreferences(
		PREFERENCES_NAME,
		Context.MODE_PRIVATE
	)

	companion object {

		@Volatile
		private var instance: PersistentStorage? = null

		fun getInstance(context: Context) =
			instance ?: synchronized(this) {
				instance ?: PersistentStorage(context).also { instance = it }
			}
	}

	suspend fun saveRecentSong(description: MediaDescriptionCompat, position: Long) {

		withContext(Dispatchers.IO) {
			preferences.edit()
				.putString(RECENT_SONG_MEDIA_ID_KEY, description.mediaId)
				.putString(RECENT_SONG_TITLE_KEY, description.title.toString())
				.putString(RECENT_SONG_SUBTITLE_KEY, description.subtitle.toString())
				.putString(RECENT_SONG_ICON_URI_KEY, description.iconUri.toString())
				.putLong(RECENT_SONG_POSITION_KEY, position)
				.apply()
		}
	}

	fun loadRecentSong(): MediaBrowserCompat.MediaItem? {
		val mediaId = preferences.getString(RECENT_SONG_MEDIA_ID_KEY, null)
		return if (mediaId == null) {
			null
		} else {
			val extras = Bundle().also {
				val position = preferences.getLong(RECENT_SONG_POSITION_KEY, 0L)
				it.putLong(MEDIA_DESCRIPTION_EXTRAS_START_PLAYBACK_POSITION_MS, position)
			}

			MediaBrowserCompat.MediaItem(
				MediaDescriptionCompat.Builder()
					.setMediaId(mediaId)
					.setTitle(preferences.getString(RECENT_SONG_TITLE_KEY, ""))
					.setSubtitle(preferences.getString(RECENT_SONG_SUBTITLE_KEY, ""))
					.setIconUri(Uri.parse(preferences.getString(RECENT_SONG_ICON_URI_KEY, "")))
					.setExtras(extras)
					.build(), FLAG_PLAYABLE
			)
		}
	}
}

private const val PREFERENCES_NAME = "howl"
private const val RECENT_SONG_MEDIA_ID_KEY = "recent_song_media_id"
private const val RECENT_SONG_TITLE_KEY = "recent_song_title"
private const val RECENT_SONG_SUBTITLE_KEY = "recent_song_subtitle"
private const val RECENT_SONG_ICON_URI_KEY = "recent_song_icon_uri"
private const val RECENT_SONG_POSITION_KEY = "recent_song_position"