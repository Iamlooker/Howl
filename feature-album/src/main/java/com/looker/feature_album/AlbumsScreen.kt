package com.looker.feature_album

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.looker.core_ui.components.BottomSheets
import com.looker.core_ui.components.HowlImage
import com.looker.core_ui.ext.backgroundGradient
import com.looker.core_ui.components.rememberDominantColorState
import com.looker.core_model.Album
import com.looker.core_ui.components.LoadingState
import com.looker.core_ui.components.SongUiState
import com.looker.core_ui.components.SongsList
import com.looker.core_ui.components.SwitchPreference
import com.looker.feature_album.sheet.DetailSheetContent
import com.looker.feature_album.sheet.DetailsText
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AlbumRoute(viewModel: AlbumsViewModel = hiltViewModel()) {
	val albums by viewModel.albumsState.collectAsState()
	val songs by viewModel.songsState.collectAsState()
	val currentAlbum by viewModel.currentAlbum.collectAsState()
	val bottomSheetState =
		rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
	val scope = rememberCoroutineScope()
	val bottomSheetDominantColorState = rememberDominantColorState()
	BottomSheets(
		modifier = Modifier.fillMaxSize(),
		state = bottomSheetState,
		sheetContent = {
			DetailSheetContent(
				modifier = Modifier.backgroundGradient(bottomSheetDominantColorState.color.copy(0.4f)),
				albumText = {
					DetailsText(albumName = currentAlbum.name, artistName = currentAlbum.artist)
				},
				preference = {
					val checked by remember {
						derivedStateOf {
							if (songs.songsState is SongUiState.Success)
								(songs.songsState as SongUiState.Success).songsAreBlacklisted
							else false
						}
					}
					SwitchPreference(
						text = "Hide these songs from Home page",
						checked = checked,
						onCheckedChange = { viewModel.blacklistSongs(currentAlbum, it) }
					)
				},
				songsList = {
					Surface(
						modifier = Modifier.padding(16.dp),
						shape = MaterialTheme.shapes.medium,
						color = MaterialTheme.colors.background
					) {
						SongsList(songs.songsState) { viewModel.playSong(it) }
					}
				}
			) {
				LaunchedEffect(currentAlbum) {
					bottomSheetDominantColorState.updateColorsFromImageUrl(currentAlbum.albumArt)
				}
				HowlImage(
					modifier = Modifier.matchParentSize(),
					data = currentAlbum.albumArt,
					shape = MaterialTheme.shapes.large
				)
			}
		}
	) {
		LazyVerticalGrid(columns = GridCells.Fixed(2)) {
			albumsList(albums.albumsState) {
				viewModel.setCurrentAlbum(it)
				scope.launch { bottomSheetState.show() }
			}
		}
	}
}

private fun LazyGridScope.albumsList(albums: AlbumUiState, onClick: (Album) -> Unit = {}) {
	when (albums) {
		is AlbumUiState.Success -> items(items = albums.albums, key = { it.albumId }) {
			AlbumItem(album = it, cardWidth = 150.dp, onClick = { onClick(it) })
		}
		AlbumUiState.Loading -> item { LoadingState() }
		AlbumUiState.Error -> item { Text(text = "Error") }
	}
}