package com.looker.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SwitchPreference(
	modifier: Modifier = Modifier,
	text: String,
	checked: Boolean,
	onCheckedChange: (Boolean) -> Unit
) {
	Surface(
		modifier = modifier.fillMaxWidth(),
		shape = MaterialTheme.shapes.large
	) {
		Row(
			modifier = Modifier.padding(
				horizontal = 16.dp,
				vertical = 10.dp
			),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			Text(text = text)
			Switch(checked = checked, onCheckedChange = onCheckedChange)
		}
	}
}