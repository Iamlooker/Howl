package com.looker.player_service

import android.content.Context
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
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object PlayerModule {

    @Singleton
    @Provides
    fun provideAudioRenderer(
        @ApplicationContext context: Context
    ): RenderersFactory = RenderersFactory { handler, _, audioListener, _, _ ->
        arrayOf(
            MediaCodecAudioRenderer(
                context, MediaCodecSelector.DEFAULT, handler, audioListener
            )
        )
    }

    @Singleton
    @Provides
    fun provideAudioExtractor(): ExtractorsFactory = ExtractorsFactory {
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

    @Singleton
    @Provides
    fun provideExoPlayer(
        @ApplicationContext context: Context,
        audioRenderer: RenderersFactory,
        extractorsFactory: ExtractorsFactory
    ): SimpleExoPlayer =
        SimpleExoPlayer.Builder(context, audioRenderer, extractorsFactory).build()
}