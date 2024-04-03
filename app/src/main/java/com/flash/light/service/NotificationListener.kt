package com.flash.light.service

import android.os.Handler
import android.os.Looper
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.flash.light.utils.FlashHelper
import com.flash.light.utils.SpManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.jvm.internal.Intrinsics

@AndroidEntryPoint
class NotificationListener : NotificationListenerService() {
    @Inject
    lateinit var spManager: SpManager
    @Inject
    lateinit var flashHelper: FlashHelper

    override fun onNotificationPosted(statusBarNotification: StatusBarNotification) {
        if (!Intrinsics.areEqual(
                statusBarNotification.packageName as Any,
                "sms_default_application" as Any
            ) && !Intrinsics.areEqual(
                statusBarNotification.packageName as Any,
                "com.google.android.apps.messaging" as Any
            )
        ) {
            val state = spManager.getTurnOnNotification()
            if(state){
                val onTime = spManager.getOnTimeFlashNotificationMS()
                val offTime = spManager.getOffTimeFlashNotificationMS()
                flashHelper.start(this, onTime, offTime)
                Handler(Looper.getMainLooper()).postDelayed({
                    flashHelper.stop()
                }, 5000)
            }
        } else{
            val state = spManager.getTurnOnSMS()
            if(state){
                val onTime = spManager.getOnTimeFlashSMSMS()
                val offTime = spManager.getOffTimeFlashSMSMS()
                flashHelper.start(this, onTime, offTime)
                Handler(Looper.getMainLooper()).postDelayed({
                    flashHelper.stop()
                }, 5000)
            }
        }
    }
}