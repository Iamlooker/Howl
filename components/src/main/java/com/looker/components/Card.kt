package com.looker.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.semantics
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
	onClick: () -> Unit,
	content: @Composable () -> Unit,
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
	onClick: () -> Unit,
) {
	MaterialCard(
		modifier = modifier,
		shape = imageShape,
		onClick = onClick
	) {
		SmallCardItem(
			modifier = Modifier.padding(8.dp),
			imageUrl = imageUrl,
			imageShape = imageShape,
			imageSize = imageSize,
			title = title,
			secondaryText = subText
		)
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
	onClick: () -> Unit,
) {
	MaterialCard(
		modifier = modifier.padding(8.dp).clip(imageShape).drawBehind { drawRect(cardColor) },
		backgroundColor = Color.Transparent,
		onClick = onClick
	) {
		LargeCardItem(
			imageUrl = imageUrl,
			title = title,
			secondaryText = subText,
			imageSize = imageSize,
			imageShape = imageShape
		)
	}
}

@Composable
private fun SmallCardItem(
	modifier: Modifier = Modifier,
	imageUrl: String?,
	imageSize: Dp,
	imageShape: CornerBasedShape,
	title: String?,
	secondaryText: String?,
) {
	Row(
		modifier = modifier,
		horizontalArrangement = Arrangement.Start,
		verticalAlignment = Alignment.CenterVertically
	) {
		HowlImage(
			modifier = Modifier.size(imageSize),
			data = imageUrl,
			shape = imageShape
		)
		Spacer(modifier = Modifier.width(8.dp))
		TitleSubText(
			title = title ?: "",
			subText = secondaryText ?: "",
			itemTextAlignment = Alignment.Start,
			maxLines = 1
		)
	}
}

@Composable
private fun LargeCardItem(
	modifier: Modifier = Modifier,
	imageUrl: String?,
	title: String?,
	secondaryText: String?,
	imageSize: Dp,
	imageShape: CornerBasedShape,
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
		TitleSubText(
			title = title ?: "",
			subText = secondaryText ?: "",
			titleTextStyle = MaterialTheme.typography.h6,
			subTextTextStyle = MaterialTheme.typography.body2,
			itemTextAlignment = Alignment.CenterHorizontally,
			textAlign = TextAlign.Center,
			maxLines = 2
		)
		Spacer(modifier = Modifier.height(8.dp))
	}
}