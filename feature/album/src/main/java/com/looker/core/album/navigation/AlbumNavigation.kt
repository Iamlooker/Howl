package com.looker.core.album.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.looker.core.navigation.HowlNavigationDestination
import com.looker.core.album.AlbumRoute

object AlbumDestination : HowlNavigationDestination {
	override val route = "album_route"
	override val destination = "album_destination"
}

fun NavGraphBuilder.albumGraph() {
	composable(
		route = AlbumDestination.route
	) {
		AlbumRoute()
	}
}