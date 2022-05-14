package com.looker.howlmusic.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.BottomNavigation
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.looker.components.compositeOverBackground
import com.looker.components.localComposers.LocalDurations
import com.looker.components.localComposers.LocalElevations
import com.looker.components.tweenAnimation

@Composable
fun BottomAppBar(
	modifier: Modifier = Modifier,
	navController: NavController,
	items: List<HomeScreens>
) {
	val navBackStackEntry by navController.currentBackStackEntryAsState()
	val currentDestination = navBackStackEntry?.destination
	BottomNavigation(
		modifier = modifier
			.clip(MaterialTheme.shapes.small)
			.background(MaterialTheme.colors.surface),
		backgroundColor = Color.Transparent,
		elevation = LocalElevations.current.default
	) {
		items.forEach { screen ->
			BottomNavigationItems(
				modifier = Modifier.navigationBarsPadding(),
				icon = screen.icon,
				label = screen.title,
				selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
				onSelected = {
					navController.navigate(screen.route) {
						popUpTo(navController.graph.findStartDestination().id) { saveState = true }
						launchSingleTop = true
						restoreState = true
					}
				}
			)
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
	unselectedBackgroundColor: Color = MaterialTheme.colors.surface,
	interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
	onSelected: () -> Unit
) {

	val backgroundColor by animateColorAsState(
		targetValue = if (selected) selectedBackgroundColor.compositeOverBackground()
		else unselectedBackgroundColor,
		animationSpec = tweenAnimation(LocalDurations.current.crossFade)
	)

	val itemColor by animateColorAsState(
		targetValue = if (selected) selectedContentColor
		else unselectedContentColor.compositeOverBackground(0.5f),
		animationSpec = tweenAnimation(LocalDurations.current.crossFade)
	)

	val selectedLabel = if (selected) label
	else null

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
			label = selectedLabel,
			itemColor = itemColor
		)
	}
}

@Composable
fun BaselineBottomNavigationItem(
	modifier: Modifier = Modifier,
	icon: ImageVector,
	label: String?,
	itemColor: Color
) {
	Row(
		modifier = modifier,
		horizontalArrangement = Arrangement.Center,
		verticalAlignment = Alignment.CenterVertically
	) {
		Icon(imageVector = icon, tint = itemColor, contentDescription = null)
		Spacer(modifier = Modifier.width(4.dp))
		Text(
			modifier = Modifier.animateContentSize(tweenAnimation()),
			text = label ?: "",
			color = itemColor,
			fontWeight = FontWeight.SemiBold
		)
	}
}