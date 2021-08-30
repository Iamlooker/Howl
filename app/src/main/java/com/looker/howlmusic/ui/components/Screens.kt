package com.looker.howlmusic.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Album
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.looker.howlmusic.checkReadPermission
import com.looker.howlmusic.ui.components.MainScreens.HOME
import com.looker.howlmusic.ui.components.MainScreens.ON_BOARDING
import com.looker.onboarding.OnBoardingPage
import com.looker.ui_albums.Albums
import com.looker.ui_songs.Songs

object MainScreens {
    const val ON_BOARDING = "on-boarding"
    const val HOME = "home"
}

sealed class HomeScreens(
    val title: String,
    val icon: ImageVector,
    val route: String,
) {
    object SONGS : HomeScreens("Songs", Icons.Rounded.MusicNote, "$HOME/songs")
    object ALBUMS : HomeScreens("Albums", Icons.Rounded.Album, "$HOME/albums")
}

@Composable
fun HomeNavGraph(navController: NavHostController) {
    val startDestination = if (!checkReadPermission(LocalContext.current)) ON_BOARDING
    else HOME
    NavHost(
        navController = navController,
        startDestination = startDestination,
        builder = {
            composable(ON_BOARDING) {
                OnBoardingPage {
                    navController.navigate(HOME)
                }
            }

            navigation(
                route = HOME,
                startDestination = HomeScreens.SONGS.route
            ) {
                composable(HomeScreens.SONGS.route) { Songs() }
                composable(HomeScreens.ALBUMS.route) { Albums() }
            }
        }
    )
}