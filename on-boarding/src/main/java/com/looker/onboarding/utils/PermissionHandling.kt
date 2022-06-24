package com.looker.onboarding.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.core.content.ContextCompat

val PERMISSION31 = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) "READ_MEDIA_AUDIO" else "READ_EXTERNAL_STORAGE"

val READ_PERMISSION = "android.permission.$PERMISSION31"

fun checkReadPermission(context: Context) =
	ContextCompat.checkSelfPermission(
		context, READ_PERMISSION
	) == PackageManager.PERMISSION_GRANTED

fun askReadPermission(launcher: ManagedActivityResultLauncher<String, Boolean>) {
	launcher.launch(READ_PERMISSION)
}

inline fun handlePermissions(
	context: Context,
	launcher: ManagedActivityResultLauncher<String, Boolean>,
	onGranted: () -> Unit,
	onDenied: () -> Unit,
) {
	if (checkReadPermission(context)) {
		onGranted()
	} else {
		onDenied()
		askReadPermission(launcher)
	}
}