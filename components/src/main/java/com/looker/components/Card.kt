package com.looker.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.looker.components.ComponentConstants.tweenAnimation

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MaterialCard(
    modifier: Modifier = Modifier,
    elevation: Dp = 0.dp,
    contentColor: Color = MaterialTheme.colors.onBackground,
    backgroundColor: Color = MaterialTheme.colors.background,
    rippleColor: Color = MaterialTheme.colors.primary,
    shape: Shape = MaterialTheme.shapes.medium,
    onClick: () -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit
) {
    val animateColor by animateColorAsState(
        targetValue = backgroundColor,
        animationSpec = tweenAnimation()
    )

    Card(
        modifier = modifier,
        elevation = elevation,
        shape = shape,
        backgroundColor = animateColor,
        contentColor = contentColor,
        indication = rememberRipple(color = rippleColor),
        interactionSource = interactionSource,
        content = content,
        onClick = onClick,
    )
}

@Composable
fun ItemCard(
    modifier: Modifier = Modifier,
    imageUrl: String,
    title: String,
    subText: String,
    imageSize: Dp,
    imageShape: CornerBasedShape = MaterialTheme.shapes.medium,
    cardColor: Color = Color.Transparent,
    rippleColor: Color = Color.Unspecified,
    onClick: () -> Unit = {}
) {
    MaterialCard(
        modifier = modifier.padding(8.dp),
        backgroundColor = cardColor,
        rippleColor = rippleColor,
        onClick = onClick
    ) {
        if (imageSize > 100.dp) {
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                HowlImage(
                    modifier = Modifier.size(imageSize),
                    data = imageUrl,
                    imageFillerColor = cardColor,
                    shape = imageShape,
                    onClick = onClick
                )
                ItemCardText(
                    title = title,
                    subText = subText,
                    titleTextStyle = MaterialTheme.typography.h6,
                    subTextTextStyle = MaterialTheme.typography.body2,
                    textAlignment = Alignment.CenterHorizontally
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        } else {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                HowlImage(
                    modifier = Modifier.size(imageSize),
                    data = imageUrl,
                    shape = MaterialTheme.shapes.medium
                )
                Spacer(modifier = Modifier.width(8.dp))
                ItemCardText(
                    title = title,
                    subText = subText,
                    titleTextStyle = MaterialTheme.typography.body1,
                    subTextTextStyle = MaterialTheme.typography.caption,
                    textAlignment = Alignment.Start
                )
            }
        }
    }
}

@Composable
fun ItemCardText(
    modifier: Modifier = Modifier,
    title: String,
    titleTextStyle: TextStyle = MaterialTheme.typography.body1,
    subText: String,
    subTextTextStyle: TextStyle = MaterialTheme.typography.caption,
    textAlignment: Alignment.Horizontal
) {
    Column(
        modifier = modifier.padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = textAlignment
    ) {
        WrappedText(
            text = title,
            style = titleTextStyle
        )
        WrappedText(
            text = subText,
            style = subTextTextStyle
        )
    }
}