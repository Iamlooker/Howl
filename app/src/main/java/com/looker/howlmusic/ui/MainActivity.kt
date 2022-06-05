package com.looker.howlmusic.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.looker.core_database.dao.SongDao
import com.looker.howlmusic.App
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	@Inject
	lateinit var songDao: SongDao
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		lifecycleScope.launch {
			songDao.getSongEntitiesStream().collectLatest {
				Log.e("List", it.toString())
			}
		}
		setContent { App() }
		WindowCompat.setDecorFitsSystemWindows(window, false)
	}
}