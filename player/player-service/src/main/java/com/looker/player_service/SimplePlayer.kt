package com.looker.player_service

import android.net.Uri
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer

object SimplePlayer {
    fun playSong(player: SimpleExoPlayer, songUri: Uri) {
        player.stop()
        player.clearMediaItems()
        player.setMediaItem(MediaItem.fromUri(songUri))
        player.prepare()
        player.play()
    }
}