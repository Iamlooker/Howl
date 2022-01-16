package com.looker.onboarding.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.core.content.ContextCompat

const val READ_PERMISSION = "android.permission.READ_EXTERNAL_STORAGE"

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