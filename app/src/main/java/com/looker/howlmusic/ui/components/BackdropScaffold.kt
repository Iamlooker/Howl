package com.looker.howlmusic.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.looker.components.ComponentConstants.tweenAnimation
import com.looker.components.SheetsState
import com.looker.components.backgroundGradient
import com.looker.components.rememberDominantColorState
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun Backdrop(
    modifier: Modifier = Modifier,
    state: BackdropScaffoldState,
    backdropValue: SheetsState,
    playing: Boolean,
    enableGesture: Boolean = true,
    albumArt: String? = null,
    header: @Composable () -> Unit,
    backLayerContent: @Composable () -> Unit,
    frontLayerContent: @Composable () -> Unit
) {
    val materialBackground = MaterialTheme.colors.background
    val materialSurface = MaterialTheme.colors.surface

    val backgroundColor = rememberDominantColorState()
    var backLayerColorState by remember { mutableStateOf(materialSurface) }
    LaunchedEffect(albumArt) {
        launch {
            backgroundColor.updateColorsFromImageUrl(albumArt.toString())
            backLayerColorState = if (albumArt == null) materialSurface
            else materialBackground
        }
    }

    val backLayerColor by animateColorAsState(
        targetValue = backLayerColorState,
        animationSpec = tweenAnimation()
    )

    val expandedPeekHeight = with(LocalConfiguration.current) { screenHeightDp.dp / 3 }

    val animateFloat by animateFloatAsState(
        targetValue = when (backdropValue) {
            is SheetsState.VISIBLE -> 0.5f
            is SheetsState.TO_VISIBLE -> 0.4f
            is SheetsState.TO_HIDDEN -> 0.4f
            is SheetsState.HIDDEN -> 0.3f
        },
        animationSpec = tweenAnimation()
    )

    BackdropScaffold(
        modifier = modifier.backgroundGradient(
            color = backgroundColor.color.copy(0.3f),
            startYPercentage = animateFloat
        ),
        scaffoldState = state,
        appBar = header,
        backLayerContent = backLayerContent,
        frontLayerContent = frontLayerContent,
        backLayerBackgroundColor = backLayerColor,
        peekHeight = if (playing) expandedPeekHeight else 50.dp,
        frontLayerShape = MaterialTheme.shapes.large,
        gesturesEnabled = enableGesture
    )
}