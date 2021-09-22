package com.looker.howlmusic

import android.content.Context
import androidx.compose.material.BackdropScaffoldState
import androidx.compose.material.BackdropValue.Concealed
import androidx.compose.material.BackdropValue.Revealed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.exoplayer2.MediaItem
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
import com.looker.howlmusic.ui.components.SheetsState
import kotlinx.coroutines.launch
import java.time.Clock

class HowlViewModel : ViewModel() {

    private lateinit var exoPlayer: SimpleExoPlayer

    private val _playing = MutableLiveData<Boolean>()
    private val _progress = MutableLiveData<Float>()
    private val _toggleIcon = MutableLiveData<ImageVector>()
    private val _playIcon = MutableLiveData<ImageVector>()
    private val _handleIcon = MutableLiveData<ImageVector>()
    private val _currentSong = MutableLiveData<Song>()
    private val _enableGesture = MutableLiveData<Boolean>()
    private val _backdropValue = MutableLiveData<SheetsState>()
    private val _clock = MutableLiveData<Long>()

    val playing: LiveData<Boolean> = _playing
    val progress: LiveData<Float> = _progress
    val toggleIcon: LiveData<ImageVector> = _toggleIcon
    val playIcon: LiveData<ImageVector> = _playIcon
    val handleIcon: LiveData<ImageVector> = _handleIcon
    val currentSong: LiveData<Song> = _currentSong
    val enableGesture: LiveData<Boolean> = _enableGesture
    val backdropValue: LiveData<SheetsState> = _backdropValue
    val clock: LiveData<Long> = _clock

    fun buildExoPlayer(context: Context) {
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

        exoPlayer = SimpleExoPlayer.Builder(
            context,
            audioOnlyRenderersFactory,
            audioOnlyExtractorFactory
        ).build()
    }

    @ExperimentalMaterialApi
    fun setBackdropValue(state: BackdropScaffoldState) {
        _backdropValue.value = when {
            state.currentValue == Concealed && state.targetValue == Concealed -> SheetsState.HIDDEN
            state.currentValue == Revealed && state.targetValue == Concealed -> SheetsState.ToHIDDEN
            state.currentValue == Revealed && state.targetValue == Revealed -> SheetsState.VISIBLE
            state.currentValue == Concealed && state.targetValue == Revealed -> SheetsState.ToVISIBLE
            else -> SheetsState.HIDDEN
        }
    }

    fun gestureState(allowGesture: Boolean) {
        _enableGesture.value = allowGesture
    }

    fun updateTime() {
        viewModelScope.launch { _clock.value = Clock.systemDefaultZone().millis() }
    }

    private fun updatePlayIcon() {
        _playIcon.value =
            if (_playing.value == true) Icons.Rounded.Pause else Icons.Rounded.PlayArrow
    }

    fun onPlayPause() {
        if (_playing.value == true) exoPlayer.pause() else exoPlayer.play()
        _playing.value = exoPlayer.isPlaying
        updatePlayIcon()
    }

    fun updateProgress() {
        _progress.value = exoPlayer.contentPosition.toFloat() / exoPlayer.contentDuration
    }

    fun onToggle(currentState: SheetsState) {
        when (currentState) {
            SheetsState.HIDDEN -> onPlayPause()
            SheetsState.ToHIDDEN -> onPlayPause()
            SheetsState.ToVISIBLE -> {
            }
            SheetsState.VISIBLE -> {
            }
        }
    }

    fun setToggleIcon(currentState: SheetsState) {
        _toggleIcon.value = when (currentState) {
            SheetsState.HIDDEN -> _playIcon.value ?: Icons.Rounded.PlayArrow
            SheetsState.ToHIDDEN -> _playIcon.value ?: Icons.Rounded.PlayArrow
            SheetsState.ToVISIBLE -> Icons.Rounded.Shuffle
            SheetsState.VISIBLE -> Icons.Rounded.Shuffle
        }
    }

    fun setHandleIcon(currentState: SheetsState) {
        _handleIcon.value = when (currentState) {
            SheetsState.HIDDEN -> Icons.Rounded.KeyboardArrowDown
            SheetsState.ToHIDDEN -> Icons.Rounded.KeyboardArrowDown
            SheetsState.VISIBLE -> Icons.Rounded.KeyboardArrowUp
            SheetsState.ToVISIBLE -> Icons.Rounded.KeyboardArrowUp
        }
    }

    fun onSongClicked(song: Song) {
        _playing.value = true
        _currentSong.value = song
        exoPlayer.apply {
            clearMediaItems()
            setMediaItem(MediaItem.fromUri(song.songUri))
            prepare()
            play()
        }
        updatePlayIcon()
    }

    fun onSeek(seekTo: Float) {
        _progress.value = seekTo
        exoPlayer.seekTo((exoPlayer.contentDuration * seekTo).toLong())
    }

    fun playNext() {
        exoPlayer.seekToNext()
    }

    fun playPrevious() {
        exoPlayer.seekToPrevious()
    }

    override fun onCleared() {
        super.onCleared()
        exoPlayer.release()
    }
}
