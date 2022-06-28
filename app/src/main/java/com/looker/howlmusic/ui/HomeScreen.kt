package com.looker.howlmusic.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.material.BackdropValue.Concealed
import androidx.compose.material.BackdropValue.Revealed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.looker.core_common.states.SheetsState.HIDDEN
import com.looker.core_common.states.SheetsState.VISIBLE
import com.looker.core_ui.components.HandleIcon
import com.looker.core_ui.components.OpaqueIconButton
import com.looker.core_ui.components.overBackground
import com.looker.feature_player.Controls
import com.looker.feature_player.PlayerHeader
import com.looker.howlmusic.navigation.TopLevelNavigation
import com.looker.howlmusic.ui.components.Backdrop
import com.looker.howlmusic.ui.components.BottomAppBar
import com.looker.howlmusic.ui.components.HomeNavGraph
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Home(
	navController: NavHostController,
	viewModel: HowlViewModel = viewModel()
) {
	val state = rememberBackdropScaffoldState(
		initialValue = Concealed,
		confirmStateChange = {
			viewModel.setBackDrop(
				when (it) {
					Concealed -> HIDDEN
					Revealed -> VISIBLE
				}
			)
			true
		}
	)
	val backdropValue by viewModel.backdropValue.collectAsState()

	Backdrop(
		state = state,
		isPlaying = {
			val expandedHeight =
				WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 270.dp
			val isPlaying by viewModel.isPlaying.collectAsState()
			animateDpAsState(
				targetValue = if (isPlaying) expandedHeight
				else WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
			).value
		},
		header = {
			PlayerHeader { backdropValue }
		},
		frontLayerContent = {
			val scope = rememberCoroutineScope()
			FrontLayer(
				navController = navController,
				handleIcon = {
					if (backdropValue == VISIBLE) 0f else 1f
				},
				openPlayer = {
					scope.launch {
						viewModel.setBackDrop(VISIBLE)
						state.animateTo(Revealed, anim = spring(stiffness = Spring.StiffnessLow))
					}
				}
			)
		},
		backLayerContent = { Controls() }
	)
}

@Composable
fun FrontLayer(
	navController: NavHostController,
	handleIcon: () -> Float,
	openPlayer: () -> Unit
) {
	val topLevelNavigation = remember(navController) {
		TopLevelNavigation(navController)
	}
	Scaffold(
		bottomBar = {
			val navBackStackEntry by navController.currentBackStackEntryAsState()
			val currentDestination = navBackStackEntry?.destination
			BottomAppBar(
				modifier = Modifier.windowInsetsBottomHeight(
					WindowInsets.navigationBars.add(WindowInsets(bottom = 56.dp))
				),
				currentDestination = currentDestination,
				onNavigate = topLevelNavigation::navigateUp
			)
		},
		floatingActionButton = {
			OpaqueIconButton(
				backgroundColor = MaterialTheme.colors.primaryVariant.overBackground(),
				contentPadding = PaddingValues(vertical = 16.dp),
				onClick = openPlayer,
				shape = MaterialTheme.shapes.small,
				icon = Icons.Rounded.KeyboardArrowDown
			)
		}
	) { bottomNavigationPadding ->
		Column(Modifier.padding(bottomNavigationPadding)) {
			HandleIcon(onClick = openPlayer, angle = handleIcon)
			HomeNavGraph(navController = navController)
		}
	}
}