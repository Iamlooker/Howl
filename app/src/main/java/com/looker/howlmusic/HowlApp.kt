package com.looker.howlmusic

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.looker.howlmusic.ui.navigation.BottomAppBar
import com.looker.howlmusic.ui.navigation.HomeNavGraph
import com.looker.howlmusic.ui.navigation.HomeScreens
import com.looker.howlmusic.ui.theme.HowlMusicTheme

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