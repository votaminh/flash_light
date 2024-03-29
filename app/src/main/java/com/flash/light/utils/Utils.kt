package com.flash.light.utils

import android.content.Context
import android.content.pm.PackageManager
import android.text.TextUtils
import androidx.core.content.ContextCompat

object Utils {
    fun isPermissionGranted(context: Context?, permission: String?): Boolean {
        return if (context == null || TextUtils.isEmpty(permission)) false else ContextCompat.checkSelfPermission(
            context,
            permission!!
        ) == PackageManager.PERMISSION_GRANTED
    }
}