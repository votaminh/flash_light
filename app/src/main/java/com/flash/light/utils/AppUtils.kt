package com.flash.light.utils

import android.content.Context
import android.os.BatteryManager
import android.os.PowerManager
import android.util.Log
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
    }
}