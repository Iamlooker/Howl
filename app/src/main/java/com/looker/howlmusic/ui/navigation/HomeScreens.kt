package com.looker.howlmusic.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Album
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.looker.howlmusic.checkReadPermission
import com.looker.onboarding.OnBoardingPage
import com.looker.ui_albums.Albums
import com.looker.ui_songs.Songs

fun NavGraphBuilder.addHomeGraph() {
    composable(HomeSections.SONGS.route) { Songs() }
    composable(HomeSections.ALBUMS.route) { Albums() }
}

sealed class HomeSections(
    val title: String,
    val icon: ImageVector,
    val route: String,
) {
    object SONGS : HomeSections("Songs", Icons.Rounded.MusicNote, "home/songs")
    object ALBUMS : HomeSections("Albums", Icons.Rounded.Album, "home/albums")
}

@Composable
fun HomeNavGraph(navController: NavHostController) {
    val startDestination = if (!checkReadPermission(LocalContext.current)) "on-boarding"
    else HomeSections.SONGS.route
    NavHost(
        navController = navController,
        startDestination = startDestination,
        builder = {
            composable("on-boarding") {
                OnBoardingPage {
                    navController.navigate(HomeSections.SONGS.route)
                }
            }
            addHomeGraph()
        }
    )
}