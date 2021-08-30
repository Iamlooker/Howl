package com.looker.howlmusic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.looker.howlmusic.ui.navigation.BottomAppBar
import com.looker.howlmusic.ui.navigation.HomeSections
import com.looker.howlmusic.ui.navigation.addHomeGraph
import com.looker.onboarding.OnBoardingPage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var startDestination = HomeSections.SONGS.route
        if (!checkReadPermission(this)) startDestination = "on-boarding"

        setContent {
            val items = remember { HomeSections.values() }
            val navController = rememberNavController()
            AppTheme {
                Scaffold(
                    bottomBar = {
                        BottomAppBar(navController = navController, items = items)
                    }
                ) { contentPadding ->
                    BoxWithConstraints(Modifier.padding(contentPadding)) {
                        NavHost(
                            navController = navController,
                            startDestination = startDestination,
                            builder = {
                                composable("on-boarding") {
                                    OnBoardingPage {
                                        navController.navigate(HomeSections.SONGS.route)
                                    }
                                }
                                addHomeGraph()
                            }
                        )
                    }
                }
            }
        }
    }
}