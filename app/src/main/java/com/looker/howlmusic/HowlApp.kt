package com.looker.howlmusic

import android.app.Application
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.google.accompanist.insets.ProvideWindowInsets
import com.looker.howlmusic.ui.Home
import com.looker.howlmusic.ui.theme.HowlMusicTheme
import com.looker.onboarding.OnBoardingPage
import com.looker.onboarding.utils.checkReadPermission
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HowlApp : Application(), ImageLoaderFactory {
	override fun newImageLoader(): ImageLoader = ImageLoader.Builder(this).build()
}

@Composable
fun App(viewModel: HowlViewModel) {
	val context = LocalContext.current
	var canReadStorage by remember { mutableStateOf(checkReadPermission(context)) }

	HowlMusicTheme {
		ProvideWindowInsets {
			if (canReadStorage) Home(viewModel)
			else OnBoardingPage { canReadStorage = it }
		}
	}
}