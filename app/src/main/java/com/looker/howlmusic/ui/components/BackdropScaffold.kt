package com.looker.howlmusic.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.looker.components.ComponentConstants.tweenAnimation

@ExperimentalMaterialApi
@Composable
fun Backdrop(
    modifier: Modifier = Modifier,
    state: BackdropScaffoldState,
    currentFraction: Float,
    playing: Boolean,
    header: @Composable () -> Unit,
    backLayerContent: @Composable () -> Unit,
    frontLayerContent: @Composable () -> Unit
) {

    val sheetBackgroundColor by animateColorAsState(
        targetValue = if (currentFraction < 1f) MaterialTheme.colors.surface
        else MaterialTheme.colors.background, animationSpec = tweenAnimation()
    )

    val peekHeight by animateDpAsState(
        targetValue = if (playing) 400.dp else 50.dp,
        animationSpec = tweenAnimation()
    )

    BackdropScaffold(
        modifier = modifier,
        scaffoldState = state,
        appBar = header,
        backLayerContent = backLayerContent,
        frontLayerContent = frontLayerContent,
        backLayerBackgroundColor = sheetBackgroundColor,
        peekHeight = peekHeight,
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