package com.looker.howlmusic

import android.content.ComponentName
import android.os.Bundle
import android.os.RemoteException
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaControllerCompat
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import coil.ImageLoader
import com.looker.player_service.service.PlayerService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var imageLoader: ImageLoader
    private lateinit var mediaBrowser: MediaBrowserCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent { App(imageLoader) }
        WindowCompat.setDecorFitsSystemWindows(window, false)

        mediaBrowser = MediaBrowserCompat(
            this, ComponentName(this, PlayerService::class.java),
            object : MediaBrowserCompat.ConnectionCallback() {
                override fun onConnected() {
                    try {
                        val token = mediaBrowser.sessionToken
                        val controller = MediaControllerCompat(this@MainActivity, token)
                        MediaControllerCompat.setMediaController(this@MainActivity, controller)
                    } catch (e: RemoteException) {
                        Log.e(
                            MainActivity::class.java.simpleName,
                            "Error creating controller", e
                        )
                    }
                }

                override fun onConnectionSuspended() {}
                override fun onConnectionFailed() {}
            }, null
        )
        mediaBrowser.connect()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaBrowser.disconnect()
    }
}