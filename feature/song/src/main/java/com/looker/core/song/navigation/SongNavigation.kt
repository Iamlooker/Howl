package com.looker.core.song.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.looker.core.navigation.HowlNavigationDestination
import com.looker.core.song.SongRoute

object SongDestination : HowlNavigationDestination {
	override val route = "song_route"
	override val destination = "song_destination"
}

fun NavGraphBuilder.songGraph() {
	composable(
		route = SongDestination.route
	) {
		SongRoute()
	}
}