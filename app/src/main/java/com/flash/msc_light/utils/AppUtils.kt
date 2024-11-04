package com.flash.msc_light.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.BatteryManager
import android.os.PowerManager
import com.flash.msc_light.R
import kotlin.jvm.internal.Intrinsics

class AppUtils {
    companion object {
        fun range(process: Int, start: Float, end: Float): Float {
            return (end - start) * process / 100 + start
        }

        fun invertRange(values: Float, start: Float, end: Float): Float {
            return ((values - start) * 100) / (end - start);
        }

        fun isScreenOn(context: Context): Boolean {
            Intrinsics.checkNotNullParameter(context, "context")
            val powerManager = context.getSystemService(android.content.Context.POWER_SERVICE) as PowerManager
            Intrinsics.checkNotNull(powerManager)
            return powerManager.isInteractive
        }

        fun isLowBattery(context: Context): Boolean {
            Intrinsics.checkNotNullParameter(context, "context")
            val systemService = context.getSystemService(android.content.Context.BATTERY_SERVICE)
            Intrinsics.checkNotNull(
                systemService,
                "null cannot be cast to non-null type android.os.BatteryManager"
            )
            val intProperty = (systemService as BatteryManager).getIntProperty(4)
            return intProperty <= 20
        }

        fun openLink(context: Context, str: String?) {
            Intrinsics.checkNotNullParameter(context, "context")
            Intrinsics.checkNotNullParameter(str, "url")
            try {
                context.startActivity(Intent("android.intent.action.VIEW", Uri.parse(str)))
            } catch (unused: ActivityNotFoundException) {
                val intent = Intent(Intent("android.intent.action.VIEW", Uri.parse(str)))
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                }
            }
        }

        fun shareApp(context: Context) {
            Intrinsics.checkNotNullParameter(context, "context")
            try {
                val intent = Intent("android.intent.action.SEND")
                intent.type = "text/plain"
                intent.putExtra(
                    "android.intent.extra.SUBJECT",
                    context.getString(R.string.txt_share_app)
                )
                intent.putExtra(
                    "android.intent.extra.TEXT",
                    "https://play.google.com/store/apps/details?id=" + context.packageName
                )
                context.startActivity(Intent.createChooser(intent, "choose one"))
            } catch (e: Exception) {
                e.toString()
            }
        }
    }
}