package com.looker.howlmusic.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Album
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.looker.feature_album.navigation.AlbumDestination
import com.looker.feature_album.navigation.albumGraph
import com.looker.feature_song.navigation.SongDestination
import com.looker.feature_song.navigation.songGraph

enum class HomeScreens(
	val title: String,
	val icon: ImageVector,
	val route: String
) {
	SONGS("Songs", Icons.Rounded.MusicNote, SongDestination.route),
	ALBUMS("Albums", Icons.Rounded.Album, AlbumDestination.route)
}

@Composable
fun HomeNavGraph(navController: NavHostController) {
	NavHost(
		navController = navController,
		startDestination = HomeScreens.SONGS.route
	) {
		songGraph()
		albumGraph()
	}
}