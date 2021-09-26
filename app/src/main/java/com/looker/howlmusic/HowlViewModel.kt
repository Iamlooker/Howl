package com.looker.howlmusic

import android.content.Context
import androidx.compose.material.BackdropScaffoldState
import androidx.compose.material.BackdropValue.Concealed
import androidx.compose.material.BackdropValue.Revealed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Shuffle
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
import com.looker.components.SheetsState
import com.looker.domain_music.Song
import kotlinx.coroutines.launch

class HowlViewModel : ViewModel() {

    private lateinit var exoPlayer: SimpleExoPlayer

    private val _playing = MutableLiveData<Boolean>()
    private val _progress = MutableLiveData<Float>()
    private val _toggleIcon = MutableLiveData<ImageVector>()
    private val _playIcon = MutableLiveData<ImageVector>()
    private val _handleIcon = MutableLiveData<Float>()
    private val _currentSong = MutableLiveData<Song>()
    private val _enableGesture = MutableLiveData<Boolean>()
    private val _backdropValue = MutableLiveData<SheetsState>()

    val playing: LiveData<Boolean> = _playing
    val progress: LiveData<Float> = _progress
    val toggleIcon: LiveData<ImageVector> = _toggleIcon
    val playIcon: LiveData<ImageVector> = _playIcon
    val handleIcon: LiveData<Float> = _handleIcon
    val currentSong: LiveData<Song> = _currentSong
    val enableGesture: LiveData<Boolean> = _enableGesture
    val backdropValue: LiveData<SheetsState> = _backdropValue

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
            state.isConcealed -> SheetsState.HIDDEN
            state.currentValue == Revealed && state.targetValue == Concealed -> SheetsState.ToHIDDEN
            state.isRevealed -> SheetsState.VISIBLE
            state.currentValue == Concealed && state.targetValue == Revealed -> SheetsState.ToVISIBLE
            else -> SheetsState.HIDDEN
        }
    }

    fun gestureState(allowGesture: Boolean) {
        _enableGesture.value = allowGesture
    }

    private fun updatePlayIcon(isPlaying: Boolean) {
        _playIcon.value =
            if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow
    }

    fun onPlayPause() {
        if (_playing.value == true) exoPlayer.pause() else exoPlayer.play()
        _playing.value = exoPlayer.isPlaying
        updatePlayIcon(exoPlayer.isPlaying)
    }

    fun onToggle(currentState: SheetsState) {
        when (currentState) {
            SheetsState.HIDDEN -> onPlayPause()
            SheetsState.ToHIDDEN -> onPlayPause()
            else -> {
            }
        }
    }

    fun setToggleIcon(currentState: SheetsState) {
        _toggleIcon.value = when (currentState) {
            SheetsState.HIDDEN -> playIcon.value ?: Icons.Rounded.PlayArrow
            SheetsState.ToHIDDEN -> playIcon.value ?: Icons.Rounded.PlayArrow
            SheetsState.ToVISIBLE -> Icons.Rounded.Shuffle
            SheetsState.VISIBLE -> Icons.Rounded.Shuffle
        }
    }

    fun setHandleIcon(currentState: SheetsState) {
        viewModelScope.launch {
            _handleIcon.value = when (currentState) {
                SheetsState.HIDDEN -> 0f
                SheetsState.ToHIDDEN -> 1f
                SheetsState.VISIBLE -> 2f
                SheetsState.ToVISIBLE -> 1f
            }
        }
    }

    fun onSongClicked(song: Song) {
        _playing.value = true
        updatePlayIcon(true)
        _currentSong.value = song
        exoPlayer.apply {
            clearMediaItems()
            setMediaItem(MediaItem.fromUri(song.songUri))
            prepare()
            play()
        }
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
