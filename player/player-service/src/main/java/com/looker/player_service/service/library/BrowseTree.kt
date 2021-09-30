package com.looker.player_service.service.library

import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import com.looker.player_service.R
import com.looker.player_service.service.extensions.*

class BrowseTree(
    val context: Context,
    musicSource: MusicSource,
    val recentMediaId: String? = null
) {
    private val mediaIdToChildren = mutableMapOf<String, MutableList<MediaMetadataCompat>>()
    val searchableByUnknownCaller = true

    init {
        val rootList = mediaIdToChildren[HOWL_BROWSABLE_ROOT] ?: mutableListOf()

        val recommendedMetadata = MediaMetadataCompat.Builder().apply {
            id = HOWL_RECOMMENDED_ROOT
            title = "Recommended"
            albumArtUri = RESOURCE_ROOT_URI +
                    context.resources.getResourceEntryName(R.drawable.exo_edit_mode_logo)
            flag = MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
        }.build()

        val albumsMetadata = MediaMetadataCompat.Builder().apply {
            id = HOWL_ALBUMS_ROOT
            title = "Albums"
            albumArtUri = RESOURCE_ROOT_URI +
                    context.resources.getResourceEntryName(R.drawable.exo_ic_chevron_left)
            flag = MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
        }.build()

        rootList += recommendedMetadata
        rootList += albumsMetadata
        mediaIdToChildren[HOWL_BROWSABLE_ROOT] = rootList

        musicSource.forEach { mediaItem ->
            val albumMediaId = mediaItem.album.urlEncoded
            val albumChildren = mediaIdToChildren[albumMediaId] ?: buildAlbumRoot(mediaItem)
            albumChildren += mediaItem

            if (mediaItem.trackNumber == 1L) {
                val recommendedChildren = mediaIdToChildren[HOWL_RECOMMENDED_ROOT]
                    ?: mutableListOf()
                recommendedChildren += mediaItem
                mediaIdToChildren[HOWL_RECOMMENDED_ROOT] = recommendedChildren
            }

            if (mediaItem.id == recentMediaId) {
                mediaIdToChildren[HOWL_RECENT_ROOT] = mutableListOf(mediaItem)
            }
        }
    }

    operator fun get(mediaId: String) = mediaIdToChildren[mediaId]

    private fun buildAlbumRoot(mediaItem: MediaMetadataCompat): MutableList<MediaMetadataCompat> {
        val albumMetadata = MediaMetadataCompat.Builder().apply {
            id = mediaItem.album.urlEncoded
            title = mediaItem.album
            artist = mediaItem.artist
            albumArt = mediaItem.albumArt
            albumArtUri = mediaItem.albumArtUri.toString()
            flag = MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
        }.build()

        val rootList = mediaIdToChildren[HOWL_ALBUMS_ROOT] ?: mutableListOf()
        rootList += albumMetadata
        mediaIdToChildren[HOWL_ALBUMS_ROOT] = rootList

        return mutableListOf<MediaMetadataCompat>().also {
            mediaIdToChildren[albumMetadata.id!!] = it
        }
    }
}

const val HOWL_BROWSABLE_ROOT = "/"
const val HOWL_EMPTY_ROOT = "@empty@"
const val HOWL_RECOMMENDED_ROOT = "__RECOMMENDED__"
const val HOWL_ALBUMS_ROOT = "__ALBUMS__"
const val HOWL_RECENT_ROOT = "__RECENT__"

const val MEDIA_SEARCH_SUPPORTED = "android.media.browse.SEARCH_SUPPORTED"

const val RESOURCE_ROOT_URI = "android.resource://com.looker.howlmusic/drawable/"