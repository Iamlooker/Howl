package com.looker.howlmusic.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.looker.constants.Constants.READ_PERMISSION

fun checkReadPermission(context: Context) =
    ContextCompat.checkSelfPermission(
        context, READ_PERMISSION
    ) == PackageManager.PERMISSION_GRANTED
