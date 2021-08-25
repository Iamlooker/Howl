package com.looker.howlmusic.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Album
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.looker.ui_albums.Albums
import com.looker.ui_songs.Songs

fun NavGraphBuilder.addHomeGraph() {
    composable(HomeSections.SONGS.route) { Songs() }
    composable(HomeSections.ALBUMS.route) { Albums() }
}

enum class HomeSections(
    val title: String,
    val icon: ImageVector,
    val route: String,
) {
    SONGS("Songs", Icons.Rounded.MusicNote, "home/songs"),
    ALBUMS("Albums", Icons.Rounded.Album, "home/albums")
}