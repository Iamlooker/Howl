package com.looker.howlmusic

import android.annotation.SuppressLint
import android.app.WallpaperManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.looker.components.WallpaperTheme
import com.looker.components.rememberWallpaperColor
import com.looker.howlmusic.ui.components.BottomAppBar
import com.looker.howlmusic.ui.components.HomeNavGraph
import com.looker.howlmusic.ui.components.HomeScreens
import com.looker.howlmusic.ui.theme.HowlMusicTheme
import com.looker.onboarding.OnBoardingPage

@Composable
fun HowlApp() {
    val context = LocalContext.current
    var canReadStorage by remember { mutableStateOf(checkReadPermission(context)) }

    if (canReadStorage) AppTheme()
    else OnBoardingPage {
        canReadStorage = it
    }
}

@SuppressLint("MissingPermission")
@Composable
fun AppTheme() {
    HowlMusicTheme {
        val context = LocalContext.current

        val wallpaperManager = WallpaperManager.getInstance(context)
        val wallpaperBitmap = wallpaperManager.drawable.toBitmap()

        val dominantColor = rememberWallpaperColor()

        LaunchedEffect(wallpaperBitmap) {
            dominantColor.updateColorsFromBitmap(wallpaperBitmap)
        }

        WallpaperTheme(dominantColor) {
            val items = listOf(
                HomeScreens.SONGS,
                HomeScreens.ALBUMS
            )
            val navController = rememberNavController()
            ProvideWindowInsets {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Scaffold(
                        bottomBar = {
                            BottomAppBar(
                                modifier = Modifier.navigationBarsPadding(),
                                navController = navController,
                                items = items
                            )
                        }
                    ) {
                        Box(modifier = Modifier.padding(it)) {
                            HomeNavGraph(navController = navController)
                        }
                    }
                }
            }
        }
    }
}