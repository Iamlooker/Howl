package com.looker.core_ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.looker.core_ui.localComposers.LocalDurations

@Composable
fun AnimatedText(
	text: String,
	modifier: Modifier = Modifier,
	style: TextStyle = MaterialTheme.typography.h4,
	maxLines: Int = 1,
	overflow: TextOverflow = TextOverflow.Ellipsis,
	textAlign: TextAlign = TextAlign.Center,
	fontWeight: FontWeight = FontWeight.Medium
) {
	Text(
		modifier = modifier.animateContentSize(tween(LocalDurations.current.fadeIn)),
		text = text,
		style = style,
		maxLines = maxLines,
		overflow = overflow,
		textAlign = textAlign,
		fontWeight = fontWeight
	)
}

@Composable
fun TitleSubText(
	modifier: Modifier = Modifier,
	title: String,
	subText: String,
	titleTextStyle: TextStyle = MaterialTheme.typography.body1,
	subTextTextStyle: TextStyle = MaterialTheme.typography.caption,
	itemTextAlignment: Alignment.Horizontal,
	textColor: Color = MaterialTheme.colors.onBackground,
	textAlign: TextAlign = TextAlign.Start,
	maxLines: Int = 3
) {
	Column(
		modifier = modifier.padding(horizontal = 8.dp),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = itemTextAlignment
	) {
		Text(
			text = title,
			style = titleTextStyle,
			textAlign = textAlign,
			color = textColor,
			maxLines = maxLines,
			overflow = TextOverflow.Ellipsis
		)
		Text(
			text = subText,
			style = subTextTextStyle,
			textAlign = textAlign,
			color = textColor.copy(0.7f),
			maxLines = maxLines,
			overflow = TextOverflow.Ellipsis
		)
	}
}