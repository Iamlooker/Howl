package com.looker.howlmusic

import android.content.Context
import androidx.compose.material.BackdropScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowDropUp
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.exoplayer2.RenderersFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.audio.MediaCodecAudioRenderer
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.google.android.exoplayer2.extractor.flac.FlacExtractor
import com.google.android.exoplayer2.extractor.mp3.Mp3Extractor
import com.google.android.exoplayer2.extractor.mp4.Mp4Extractor
import com.google.android.exoplayer2.extractor.ogg.OggExtractor
import com.google.android.exoplayer2.extractor.ts.Ac3Extractor
import com.google.android.exoplayer2.extractor.ts.AdtsExtractor
import com.google.android.exoplayer2.extractor.wav.WavExtractor
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector
import com.looker.domain_music.Song
import com.looker.player_service.service.PlayerService

class HowlViewModel : ViewModel() {

    lateinit var player: SimpleExoPlayer

    private val _playing = MutableLiveData<Boolean>()
    private val _shuffle = MutableLiveData<Boolean>()
    private val _progress = MutableLiveData<Float>()
    private val _playIcon = MutableLiveData<ImageVector>()
    private val _handleIcon = MutableLiveData<ImageVector>()
    private val _currentSong = MutableLiveData<Song>()

    val playing: LiveData<Boolean> = _playing
    val shuffle: LiveData<Boolean> = _shuffle
    val progress: LiveData<Float> = _progress
    val handleIcon: LiveData<ImageVector> = _handleIcon
    val currentSong: LiveData<Song> = _currentSong
    val playIcon: LiveData<ImageVector>
        get() {
            _playIcon.value = if (_playing.value == true) Icons.Rounded.Pause
            else Icons.Rounded.PlayArrow
            return _playIcon
        }

    fun buildPlayer(context: Context) {
        val audioOnlyRenderersFactory =
            RenderersFactory { handler, _, audioListener, _, _ ->
                arrayOf(
                    MediaCodecAudioRenderer(
                        context, MediaCodecSelector.DEFAULT, handler, audioListener
                    )
                )
            }

        val audioOnlyExtractorFactory = ExtractorsFactory {
            arrayOf(
                Mp3Extractor(),
                WavExtractor(),
                AdtsExtractor(),
                OggExtractor(),
                Ac3Extractor(),
                Mp4Extractor(),
                FlacExtractor()
            )
        }

        player = SimpleExoPlayer.Builder(
            context,
            audioOnlyRenderersFactory,
            audioOnlyExtractorFactory
        ).build()
    }

    fun onPlayPause() {
        if (player.isPlaying) player.pause()
        else player.play()
        _playing.value = player.isPlaying
    }

    fun onToggle(playerVisible: Boolean) {
        if (playerVisible) {
            player.shuffleModeEnabled = _shuffle.value ?: false
        } else onPlayPause()
    }

    fun setHandleIcon(playerVisible: Boolean) {
        _handleIcon.value = if (playerVisible) Icons.Rounded.ArrowDropUp
        else Icons.Rounded.ArrowDropDown
    }

    fun onSongClicked(playerService: PlayerService, song: Song) {
        _playing.value = true
        _currentSong.value = song
        playerService.playSong(song.songUri)
    }

    fun updateSeekbar(progress: Float) {
        _progress.value = progress
    }

    fun onSeek(seekTo: Float) {
        _progress.value = seekTo
        seekSongTo(seekTo)
    }

    fun seekSongTo(progress: Float) {
        val long: Long? = (_currentSong.value?.duration?.times(progress))?.toLong()
        player.seekTo(long ?: 0)
    }

    fun playNext() {
        if (player.hasNextWindow()) player.seekToNext()
    }

    fun playPrevious() {
        if (player.hasPreviousWindow()) player.seekToPrevious()
    }

    @ExperimentalMaterialApi
    fun playerVisible(state: BackdropScaffoldState): Boolean = state.isRevealed

    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}
