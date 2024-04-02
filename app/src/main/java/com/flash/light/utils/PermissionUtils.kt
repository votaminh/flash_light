package com.flash.light.utils
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionUtils {
    companion object {
        fun isPhoneStatePermissionGranted(context : Context): Boolean {
            return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
        }

        fun requestPhoneStatePermission(activity : Activity, requestCode : Int) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_PHONE_STATE), requestCode)
        }

        fun isNotificationListenerPermission(context : Context): Boolean {
            val packageName: String = context.packageName
            val flat = Settings.Secure.getString(
                context.contentResolver,
                "enabled_notification_listeners"
            )
            return flat?.contains(packageName) ?: false
        }
    }
}