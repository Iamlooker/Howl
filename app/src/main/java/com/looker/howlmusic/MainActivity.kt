package com.looker.howlmusic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
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

        val startDestination = if (!checkReadPermission(this)) "on-boarding"
        else HomeSections.SONGS.route

        setContent {
            val items = remember { HomeSections.values() }
            val navController = rememberNavController()
            AppTheme {
                Scaffold(
                    bottomBar = {
                        BottomAppBar(navController = navController, items = items)
                    }
                ) { contentPadding ->
                    MainLayout(
                        startDestination = startDestination,
                        navController = navController,
                        contentPadding = contentPadding
                    )
                }
            }
        }
    }
}

@Composable
fun MainLayout(
    startDestination: String,
    navController: NavHostController,
    contentPadding: PaddingValues
) {
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