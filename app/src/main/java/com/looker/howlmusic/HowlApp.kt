package com.looker.howlmusic

import android.app.WallpaperManager
import android.graphics.Bitmap
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.looker.components.HowlSurface
import com.looker.components.rememberDominantColorState
import com.looker.howlmusic.ui.components.*
import com.looker.howlmusic.ui.theme.HowlMusicTheme
import com.looker.howlmusic.ui.theme.WallpaperTheme
import com.looker.onboarding.OnBoardingPage

@Composable
fun HowlApp() {
    val context = LocalContext.current
    var canReadStorage by remember { mutableStateOf(checkReadPermission(context)) }
    val wallpaperManager = WallpaperManager.getInstance(context)

    HowlMusicTheme {
        if (canReadStorage) {
            val wallpaperBitmap = wallpaperManager.drawable.toBitmap()
            AppTheme(wallpaperBitmap)
        } else OnBoardingPage {
            canReadStorage = it
        }
    }
}

@Composable
fun AppTheme(wallpaper: Bitmap? = null) {
    val dominantColor = rememberDominantColorState()

    LaunchedEffect(wallpaper) {
        dominantColor.updateColorsFromBitmap(wallpaper)
    }

    WallpaperTheme(dominantColor) {
        ProvideWindowInsets {
            HowlSurface(
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
            ) {
                AppContent()
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppContent() {
    val items = listOf(
        HomeScreens.SONGS,
        HomeScreens.ALBUMS
    )
    val navController = rememberNavController()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    )
    val currentFloat = bottomSheetScaffoldState.currentFraction
    Scaffold(
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.offset(0.dp, (56.dp * currentFloat)),
                navController = navController,
                items = items
            )
        }
    ) {
        AppBottomSheet(
            bottomSheetScaffoldState = bottomSheetScaffoldState,
            currentFloat = currentFloat
        ) { bottomSheetPadding ->
            Box(modifier = Modifier.padding(bottomSheetPadding)) {
                HomeNavGraph(navController = navController)
            }
        }
    }
}