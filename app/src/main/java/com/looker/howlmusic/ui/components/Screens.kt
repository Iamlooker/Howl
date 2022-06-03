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
import com.looker.constants.Resource
import com.looker.core_model.Album
import com.looker.core_model.Song
import com.looker.howlmusic.ui.ResourceAlbums
import com.looker.howlmusic.ui.ResourceSongs
import com.looker.howlmusic.ui.components.MainScreens.HOME
import com.looker.ui_albums.Albums
import com.looker.ui_songs.Songs

object MainScreens {
	const val HOME = "home"
}

enum class HomeScreens(
	val title: String,
	val icon: ImageVector,
	val route: String
) {
	SONGS("Songs", Icons.Rounded.MusicNote, "$HOME/songs"),
	ALBUMS("Albums", Icons.Rounded.Album, "$HOME/albums")
}

@Composable
fun HomeNavGraph(
	navController: NavHostController,
	songsList: ResourceSongs,
	albumsList: ResourceAlbums,
	onSongClick: (Song) -> Unit,
	onAlbumClick: (Album) -> Unit,
) {
	NavHost(
		navController = navController,
		startDestination = HomeScreens.SONGS.route
	) {
		homeGraph(
			songsList = songsList,
			albumsList = albumsList,
			onSongClick = onSongClick,
			onAlbumClick = onAlbumClick
		)
	}
}

internal fun NavGraphBuilder.homeGraph(
	songsList: ResourceSongs,
	albumsList: ResourceAlbums,
	onSongClick: (Song) -> Unit,
	onAlbumClick: (Album) -> Unit,
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
			onAlbumClick = onAlbumClick
		)
	}
}