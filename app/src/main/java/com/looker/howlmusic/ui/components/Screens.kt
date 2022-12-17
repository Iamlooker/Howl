package com.looker.howlmusic.ui.components

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.looker.core.album.navigation.albumGraph
import com.looker.core.song.navigation.SongDestination
import com.looker.core.song.navigation.songGraph

@Composable
fun HomeNavGraph(
	navController: NavHostController,
	startDestination: String = SongDestination.route
) {
	NavHost(
		navController = navController,
		startDestination = startDestination
	) {
		songGraph()
		albumGraph()
	}
}