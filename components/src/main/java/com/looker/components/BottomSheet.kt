package com.looker.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.looker.components.ComponentConstants.DefaultCrossfadeDuration
import com.looker.components.ComponentConstants.tweenAnimation

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheets(
    modifier: Modifier = Modifier,
    state: ModalBottomSheetState,
    sheetContent: @Composable ColumnScope.() -> Unit,
    content: @Composable () -> Unit
) {
    ModalBottomSheetLayout(
        modifier = modifier,
        sheetElevation = 0.dp,
        sheetState = state,
        sheetContent = sheetContent,
        content = content,
        sheetBackgroundColor = MaterialTheme.colors.background,
        scrimColor = MaterialTheme.colors.background.copy(0.3f),
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    )
}

@Composable
fun HandleIcon(icon: ImageVector, onClick: () -> Unit = {}) {
    Crossfade(
        targetState = icon,
        animationSpec = tweenAnimation(DefaultCrossfadeDuration)
    ) { currentIcon ->
        Icon(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .clickable(onClick = onClick)
                .alpha(0.6f),
            imageVector = currentIcon,
            contentDescription = "Swipe Action"
        )
    }
}

sealed class SheetsState {
    object VISIBLE : SheetsState()
    object ToVISIBLE : SheetsState()
    object ToHIDDEN : SheetsState()
    object HIDDEN : SheetsState()
}