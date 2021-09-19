package com.looker.player_service.playback

import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.looker.domain_music.Song

object Controls {

    fun SimpleExoPlayer.clearList() {
        this.clearMediaItems()
    }

    fun SimpleExoPlayer.prepareSong(songUri: String) {
        this.apply {
            setMediaItem(MediaItem.fromUri(songUri))
            prepare()
        }
    }

    fun SimpleExoPlayer.prepareList(songsList: List<Song>) {
        val mediaItems = arrayListOf<MediaItem>()
        songsList.forEach { mediaItems.add(MediaItem.fromUri(it.songUri)) }
        this.setMediaItems(mediaItems, true)
    }

    fun SimpleExoPlayer.playSong() = this.play()

    fun SimpleExoPlayer.pauseSong() = this.pause()

    fun SimpleExoPlayer.seekToFloat(float: Float) {
        this.seekTo(this.contentDuration.times(float).toLong())
    }

    fun SimpleExoPlayer.playPauseSong() {
        if (this.isPlaying) pauseSong() else playSong()
    }

    fun SimpleExoPlayer.playNextSong() {
        if (this.hasNextWindow()) this.seekToNext()
    }

    fun SimpleExoPlayer.playPreviousSong() {
        if (this.hasPreviousWindow()) this.seekToPrevious()
    }
}