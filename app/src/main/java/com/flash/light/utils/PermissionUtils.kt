package com.flash.light.utils
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

class PermissionUtils {
    companion object {
        fun isPhoneStatePermissionGranted(context : Context): Boolean {
            return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
        }

        fun requestPhoneStatePermission(activity : Activity, requestCode : Int) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_PHONE_STATE), requestCode)
        }

        fun isRecordAudioPermissionGranted(context: Context): Boolean {
            return ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
        }

        fun requestRecordAudioPermission(activity: Activity, requestCode: Int) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.RECORD_AUDIO), requestCode)
        }

        fun isNotificationListenerPermission(context : Context): Boolean {
            val packageName: String = context.packageName
            val flat = Settings.Secure.getString(
                context.contentResolver,
                "enabled_notification_listeners"
            )
            return flat?.contains(packageName) ?: false
        }

        fun requestNotificationListenerPermission(activity: Activity, requestCode: Int) {
            val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
            activity.startActivityForResult(intent, requestCode)
        }

        fun isBatteryDisable(activity: Activity): Boolean {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val packageName = activity.packageName
                val pm = activity.getSystemService(Context.POWER_SERVICE) as PowerManager
                pm.isIgnoringBatteryOptimizations(packageName)
            } else {
                true
            }
        }

        fun requestBatteryDisable(activity: Activity, requestCode: Int) {
            val intent = Intent()
            intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
            intent.data = Uri.parse("package:" + activity.packageName)
            activity.startActivityForResult(intent, requestCode)
        }

        fun permissionNotification(activity: Activity?): Boolean {
            val notificationManagerCompat = NotificationManagerCompat.from(activity!!)
            return notificationManagerCompat.areNotificationsEnabled()
        }

        fun requestNotificationPermission(activity: Activity, requestCode: Int) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.requestPermissions(
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    requestCode
                )
            }
        }
    }
}