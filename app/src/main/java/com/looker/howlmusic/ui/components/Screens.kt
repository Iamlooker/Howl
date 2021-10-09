package com.looker.howlmusic.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Album
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.looker.domain_music.Album
import com.looker.domain_music.Song
import com.looker.howlmusic.ui.components.MainScreens.HOME
import com.looker.ui_albums.Albums
import com.looker.ui_songs.Songs

object MainScreens {
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
fun HomeNavGraph(
    navController: NavHostController,
    songsList: List<Song>,
    albumsList: List<Album>,
    onSongClick: (Int) -> Unit,
    onAlbumsSheetState: (Boolean) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = HOME,
        builder = {
            homeGraph(songsList, albumsList, onAlbumsSheetState, onSongClick)
        }
    )
}

internal fun NavGraphBuilder.homeGraph(
    songsList: List<Song>,
    albumsList: List<Album>,
    onAlbumsSheetState: (Boolean) -> Unit,
    onSongClick: (Int) -> Unit
) {
    navigation(
        route = HOME,
        startDestination = HomeScreens.SONGS.route
    ) {
        composable(HomeScreens.SONGS.route) {
            Songs(
                songsList = songsList,
                onSongClick = onSongClick
            )
        }
        composable(HomeScreens.ALBUMS.route) {
            Albums(
                albumsList = albumsList,
                onStateChange = onAlbumsSheetState
            )
        }
    }
}