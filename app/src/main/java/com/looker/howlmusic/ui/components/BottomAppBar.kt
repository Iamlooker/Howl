package com.looker.howlmusic.ui.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.looker.core.ui.localComposers.LocalElevations
import com.looker.core.ui.components.overBackground
import com.looker.howlmusic.navigation.TOP_LEVEL_DESTINATIONS
import com.looker.howlmusic.navigation.TopLevelDestination

@Composable
fun BottomAppBar(
	modifier: Modifier = Modifier,
	currentDestination: NavDestination?,
	onNavigate: (TopLevelDestination) -> Unit
) {
	Surface(
		modifier = modifier,
		color = MaterialTheme.colors.surface,
		shape = MaterialTheme.shapes.small
	) {
		BottomNavigation(
			backgroundColor = Color.Transparent,
			elevation = LocalElevations.current.default
		) {
			TOP_LEVEL_DESTINATIONS.forEach { destination ->
				val selected =
					currentDestination?.hierarchy?.any { it.route == destination.route } == true

				BottomNavigationItems(
					modifier = Modifier.navigationBarsPadding(),
					icon = destination.icon,
					label = destination.label,
					selected = selected
				) {
					onNavigate(destination)
				}
			}
		}
	}
}

@Composable
fun RowScope.BottomNavigationItems(
	modifier: Modifier = Modifier,
	icon: ImageVector,
	label: String,
	selected: Boolean,
	selectedContentColor: Color = MaterialTheme.colors.primary,
	selectedBackgroundColor: Color = MaterialTheme.colors.primaryVariant,
	unselectedContentColor: Color = MaterialTheme.colors.onSurface,
	unselectedBackgroundColor: Color = Color.Transparent,
	interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
	onSelected: () -> Unit
) {

	val transition =
		updateTransition(targetState = selected, label = "Bottom Navigation Transition")

	val backgroundColor by transition.animateColor(
		transitionSpec = { spring(stiffness = Spring.StiffnessLow) }, label = ""
	) {
		if (it) selectedBackgroundColor.overBackground()
		else unselectedBackgroundColor
	}

	val itemColor by transition.animateColor(
		transitionSpec = { spring(stiffness = Spring.StiffnessLow) }, label = ""
	) {
		if (it) selectedContentColor
		else unselectedContentColor.overBackground(0.5f)
	}

	Box(
		modifier = modifier
			.weight(1f)
			.clip(MaterialTheme.shapes.small)
			.selectable(
				selected = selected,
				onClick = onSelected,
				role = Role.Tab,
				indication = null,
				interactionSource = interactionSource
			)
			.fillMaxHeight()
	) {
		BaselineBottomNavigationItem(
			modifier = Modifier
				.matchParentSize()
				.background(backgroundColor),
			icon = icon,
			label = label,
			itemColor = itemColor,
			isSelected = selected
		)
	}
}

@Composable
fun BaselineBottomNavigationItem(
	modifier: Modifier = Modifier,
	icon: ImageVector,
	label: String,
	itemColor: Color,
	isSelected: Boolean
) {
	Row(
		modifier = modifier,
		horizontalArrangement = Arrangement.Center,
		verticalAlignment = Alignment.CenterVertically
	) {
		Icon(imageVector = icon, tint = itemColor, contentDescription = null)
		Spacer(modifier = Modifier.width(4.dp))
		Text(
			modifier = Modifier.animateContentSize(
				spring(stiffness = Spring.StiffnessLow)
			),
			text = if (isSelected) label else "",
			color = itemColor,
			fontWeight = FontWeight.SemiBold
		)
	}
}