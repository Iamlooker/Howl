package com.looker.howlmusic.ui.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.looker.components.ComponentConstants.tweenAnimation
import com.looker.components.backgroundGradient
import com.looker.components.rememberDominantColorState
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun Backdrop(
    modifier: Modifier = Modifier,
    state: BackdropScaffoldState,
    currentFraction: Float,
    playing: Boolean,
    albumArt: Any? = null,
    header: @Composable () -> Unit,
    backLayerContent: @Composable () -> Unit,
    frontLayerContent: @Composable () -> Unit
) {

    val backgroundColor = rememberDominantColorState()

    LaunchedEffect(albumArt) {
        launch {
            backgroundColor.updateColorsFromImageUrl(albumArt.toString())
        }
    }

    val animateFloat by animateFloatAsState(
        targetValue = if (currentFraction == 1f) 0.5f
        else 1f,
        animationSpec = tweenAnimation(durationMillis = 700, easing = LinearOutSlowInEasing)
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
        peekHeight = if (playing) 400.dp else 50.dp,
        frontLayerShape = MaterialTheme.shapes.large
    )
}

@ExperimentalMaterialApi
val BackdropScaffoldState.currentFraction: Float
    get() {
        return try {
            val fraction = progress.fraction
            val targetValue = targetValue
            val currentValue = currentValue

            when {
                currentValue == BackdropValue.Concealed && targetValue == BackdropValue.Revealed -> 0f
                currentValue == BackdropValue.Revealed && targetValue == BackdropValue.Revealed -> 1f
                currentValue == BackdropValue.Revealed && targetValue == BackdropValue.Concealed -> fraction
                else -> 1f - fraction
            }
        } catch (e: NoSuchElementException) {
            0f
        }
    }