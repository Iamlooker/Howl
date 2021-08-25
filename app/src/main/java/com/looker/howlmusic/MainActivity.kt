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
import androidx.navigation.compose.rememberNavController
import com.looker.howlmusic.ui.navigation.BottomAppBar
import com.looker.howlmusic.ui.navigation.HomeSections
import com.looker.howlmusic.ui.navigation.addHomeGraph

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                            startDestination = HomeSections.ALBUMS.route,
                            builder = {
                                addHomeGraph()
                            }
                        )
                    }
                }
            }
        }
    }
}