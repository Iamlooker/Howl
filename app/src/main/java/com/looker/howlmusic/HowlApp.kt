package com.looker.howlmusic

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
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

@Composable
fun AppTheme() {
    HowlMusicTheme {
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