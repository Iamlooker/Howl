package com.looker.feature_song.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FilterChip
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DoneAll
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.looker.core_common.OrderType
import com.looker.core_common.order.SongOrder

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OrderChips(
	modifier: Modifier = Modifier,
	order: SongOrder,
	onOrderChange: (SongOrder) -> Unit
) {
	Row(
		modifier = modifier
			.horizontalScroll(state = rememberScrollState())
			.padding(horizontal = 8.dp)
			.background(MaterialTheme.colors.background),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(4.dp)
	) {
		OrderChip(text = "Title", isSelected = order is SongOrder.Title) {
			onOrderChange(SongOrder.Title(order.order))
		}
		OrderChip(text = "Duration", isSelected = order is SongOrder.Duration) {
			onOrderChange(SongOrder.Duration(order.order))
		}
		Spacer(
			modifier = Modifier
				.height(ChipDefaults.MinHeight - 4.dp)
				.width(1.dp)
				.background(MaterialTheme.colors.onBackground)
		)
		OrderChip(
			text = "Ascending",
			isSelected = order.order is OrderType.Ascending,
			selectedIcon = true,
			icon = {
				Icon(imageVector = Icons.Rounded.FilterList, contentDescription = null)
			}
		) {
			onOrderChange(order.copy(OrderType.Ascending))
		}
		OrderChip(
			text = "Descending",
			isSelected = order.order is OrderType.Descending,
			selectedIcon = true,
			icon = {
				Icon(
					modifier = Modifier.rotate(180f),
					imageVector = Icons.Rounded.FilterList,
					contentDescription = null
				)
			}
		) {
			onOrderChange(order.copy(OrderType.Descending))
		}
	}
}

@ExperimentalMaterialApi
@Composable
fun OrderChip(
	text: String,
	modifier: Modifier = Modifier,
	isSelected: Boolean = false,
	selectedIcon: Boolean = false,
	icon: (@Composable () -> Unit)? = null,
	onClick: () -> Unit
) {
	FilterChip(
		modifier = modifier,
		selected = isSelected,
		onClick = onClick
	) {
		if (selectedIcon && isSelected) {
			Icon(imageVector = Icons.Rounded.DoneAll, contentDescription = null)
		} else if (!isSelected && icon != null) {
			icon()
		}
		Spacer(modifier = Modifier.width(4.dp))
		Text(
			text = text,
			maxLines = 1,
			color = MaterialTheme.colors.onSurface,
			overflow = TextOverflow.Ellipsis
		)
	}
}