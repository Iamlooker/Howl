package com.looker.feature_album

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
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
import com.looker.core_model.Album
import com.looker.core_ui.components.*
import com.looker.core_ui.ext.backgroundGradient
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
				modifier = Modifier.backgroundGradient {
					bottomSheetDominantColorState.color.copy(0.4f)
				},
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
						Column {
							SongsList(songs.songsState) { viewModel.playSong(it) }
						}
					}
				}
			) {
				LaunchedEffect(currentAlbum) {
					bottomSheetDominantColorState.updateColorsFromImageUrl(currentAlbum.albumArt)
				}
				HowlImage(
					modifier = Modifier.matchParentSize(),
					shape = MaterialTheme.shapes.large,
					data = { currentAlbum.albumArt }
				)
			}
		}
	) {
		LazyVerticalGrid(columns = GridCells.Adaptive(150.dp)) {
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
			AlbumItem(album = it, cardWidth = 175.dp, onClick = { onClick(it) })
		}
		AlbumUiState.Loading -> item { LoadingState() }
		AlbumUiState.Error -> item { Text(text = "Error") }
	}
}