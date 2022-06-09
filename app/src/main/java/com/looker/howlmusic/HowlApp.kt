package com.looker.howlmusic

import android.app.Application
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.looker.data_music.GetData
import com.looker.howlmusic.ui.Home
import com.looker.howlmusic.ui.theme.HowlMusicTheme
import com.looker.onboarding.OnBoardingPage
import com.looker.onboarding.utils.checkReadPermission
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HowlApp : Application(), ImageLoaderFactory {
	override fun newImageLoader(): ImageLoader = ImageLoader.Builder(this)
		.crossfade(100)
		.error(R.drawable.error_image)
		.build()

	override fun onCreate() {
		super.onCreate()
		if (checkReadPermission(this)) GetData.initialize(this)
	}
}

@Composable
fun App() {
	HowlMusicTheme {
		val context = LocalContext.current.applicationContext
		var canReadStorage by remember { mutableStateOf(checkReadPermission(context)) }
		val navController = rememberNavController()

		if (canReadStorage) Home(navController)
		else OnBoardingPage { canReadStorage = it }
	}
}