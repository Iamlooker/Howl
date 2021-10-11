package com.looker.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.looker.components.localComposers.LocalElevations

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MaterialCard(
    modifier: Modifier = Modifier,
    elevation: Dp = LocalElevations.current.default,
    contentColor: Color = MaterialTheme.colors.onBackground,
    backgroundColor: Color = MaterialTheme.colors.background,
    shape: Shape = MaterialTheme.shapes.medium,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier.semantics(mergeDescendants = true) {},
        elevation = elevation,
        shape = shape,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        content = content,
        onClick = onClick,
    )
}

@Composable
fun SmallCard(
    modifier: Modifier = Modifier,
    imageUrl: String?,
    title: String?,
    subText: String?,
    imageSize: Dp,
    imageShape: CornerBasedShape = MaterialTheme.shapes.small,
    onClick: () -> Unit
) {
    MaterialCard(
        modifier = modifier.padding(8.dp),
        onClick = onClick
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            HowlImage(
                modifier = Modifier.size(imageSize),
                data = imageUrl,
                shape = imageShape
            )
            Spacer(modifier = Modifier.width(8.dp))
            ItemCardText(
                title = title,
                subText = subText,
                titleTextStyle = MaterialTheme.typography.body1,
                subTextTextStyle = MaterialTheme.typography.caption,
                itemTextAlignment = Alignment.Start
            )
        }
    }
}

@Composable
fun LargeCard(
    modifier: Modifier = Modifier,
    imageUrl: String?,
    title: String?,
    subText: String?,
    imageSize: Dp,
    imageShape: CornerBasedShape = MaterialTheme.shapes.medium,
    cardColor: Color = MaterialTheme.colors.surface,
    onClick: () -> Unit
) {
    MaterialCard(
        modifier = modifier.padding(8.dp),
        backgroundColor = cardColor,
        onClick = onClick
    ) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            HowlImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageSize),
                data = imageUrl,
                shape = imageShape
            )
            ItemCardText(
                title = title,
                subText = subText,
                titleTextStyle = MaterialTheme.typography.h6,
                subTextTextStyle = MaterialTheme.typography.body2,
                itemTextAlignment = Alignment.CenterHorizontally,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ItemCardText(
    modifier: Modifier = Modifier,
    title: String?,
    subText: String?,
    titleTextStyle: TextStyle = MaterialTheme.typography.body1,
    subTextTextStyle: TextStyle = MaterialTheme.typography.caption,
    itemTextAlignment: Alignment.Horizontal,
    textAlign: TextAlign = TextAlign.Start
) {
    Column(
        modifier = modifier.padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = itemTextAlignment
    ) {
        WrappedText(
            text = title,
            style = titleTextStyle,
            textAlign = textAlign,
            maxLines = 3,
        )
        WrappedText(
            text = subText,
            style = subTextTextStyle,
            textAlign = textAlign,
            maxLines = 2
        )
    }
}