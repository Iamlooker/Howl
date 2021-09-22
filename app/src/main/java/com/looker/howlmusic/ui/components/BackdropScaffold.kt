package com.looker.howlmusic.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.looker.components.ComponentConstants.calculateItemSize
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
    albumArt: Any? = null,
    header: @Composable () -> Unit,
    backLayerContent: @Composable () -> Unit,
    frontLayerContent: @Composable () -> Unit
) {

    val backgroundColor = rememberDominantColorState()

    val expandedPeekHeight by LocalContext.current.calculateItemSize(true, 3)

    LaunchedEffect(albumArt) {
        launch {
            backgroundColor.updateColorsFromImageUrl(albumArt.toString())
        }
    }

    val animateFloat by animateFloatAsState(
        targetValue = when (backdropValue) {
            is SheetsState.VISIBLE -> 0.5f
            is SheetsState.ToVISIBLE -> 0.4f
            is SheetsState.ToHIDDEN -> 0.4f
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
        backLayerBackgroundColor = MaterialTheme.colors.background,
        peekHeight = if (playing) expandedPeekHeight else 50.dp,
        frontLayerShape = MaterialTheme.shapes.large,
        gesturesEnabled = enableGesture
    )
}