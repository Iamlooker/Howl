package com.looker.howlmusic.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Album
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.looker.feature_album.navigation.AlbumDestination
import com.looker.feature_song.navigation.SongDestination

class TopLevelNavigation(private val navController: NavHostController) {
	fun navigateUp(destination: TopLevelDestination) {
		navController.navigate(destination.route) {
			popUpTo(navController.graph.findStartDestination().id) {
				saveState = true
			}
			launchSingleTop = true
			restoreState = true
		}
	}
}

@Immutable
data class TopLevelDestination(
	val route: String,
	val icon: ImageVector,
	val label: String
)

val TOP_LEVEL_DESTINATIONS = listOf(
	TopLevelDestination(
		route = SongDestination.route,
		icon = Icons.Rounded.MusicNote,
		label = "Songs"
	),
	TopLevelDestination(
		route = AlbumDestination.route,
		icon = Icons.Rounded.Album,
		label = "Albums"
	)
)