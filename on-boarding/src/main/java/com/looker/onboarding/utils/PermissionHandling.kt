package com.looker.onboarding.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.core.content.ContextCompat
import com.looker.constants.Constants.READ_PERMISSION

fun checkReadPermission(context: Context) =
    ContextCompat.checkSelfPermission(
        context, READ_PERMISSION
    ) == PackageManager.PERMISSION_GRANTED

fun askReadPermission(launcher: ManagedActivityResultLauncher<String, Boolean>) {
    launcher.launch(READ_PERMISSION)
}

fun handlePermissions(
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