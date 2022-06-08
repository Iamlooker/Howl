package com.looker.howlmusic.ui.components

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.looker.feature_album.navigation.albumGraph
import com.looker.feature_song.navigation.SongDestination
import com.looker.feature_song.navigation.songGraph

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