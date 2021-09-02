package com.looker.howlmusic.ui.components

import android.annotation.SuppressLint
import android.app.WallpaperManager
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.looker.components.calculateColorFromBitmap

@SuppressLint("MissingPermission")
@Composable
fun BottomAppBar(
    modifier: Modifier = Modifier,
    navController: NavController,
    items: List<HomeScreens>
) {

    val wallpaperManager = WallpaperManager.getInstance(LocalContext.current)
    val wallpaperBitmap = wallpaperManager.drawable.toBitmap()

    val wallpaperColor = remember { calculateColorFromBitmap(wallpaperBitmap)?.copy(0.4f) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val routes = remember { items.map { it.route } }
    if (currentDestination?.route in routes) {
        BottomNavigation(
            modifier = modifier,
            backgroundColor = wallpaperColor ?: MaterialTheme.colors.surface,
            contentColor = MaterialTheme.colors.primary,
            elevation = 0.dp
        ) {
            items.forEach { screen ->
                BottomNavigationItem(
                    icon = { Icon(screen.icon, contentDescription = null) },
                    label = { Text(screen.title) },
                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                    alwaysShowLabel = false,
                    selectedContentColor = MaterialTheme.colors.primary,
                    unselectedContentColor = MaterialTheme.colors.onSurface,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}