package com.flash.light.service

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.flash.light.utils.flash.FlashHelper
import com.flash.light.utils.SpManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotificationCloneMscListener : NotificationListenerService() {
    private val TAG = "NotificationListener"

    @Inject
    lateinit var spManager: SpManager
    @Inject
    lateinit var flashHelper: FlashHelper

    override fun onNotificationPosted(statusBarNotification: StatusBarNotification) {

        Log.i(TAG, "onNotificationPosted: ${statusBarNotification.packageName}")
        val state = spManager.getStateFlash()
        if(!state){
            return
        }

        val packageName = statusBarNotification.packageName

        if (!packageName.equals("sms_default_application") &&
            !packageName.equals("com.google.android.apps.messaging") &&
            !packageName.contains("mms") &&
            !packageName.contains("sms")
            ) {
            Log.i(TAG, "onNotificationPosted: notification")
            val state = spManager.getTurnOnNotification()
            if(state){
                val onTime = spManager.getOnTimeFlashNotificationMS()
                val offTime = spManager.getOffTimeFlashNotificationMS()
                flashHelper.startNormal(this, onTime, offTime, timeEnd = 5000)
            }
        } else{
            Log.i(TAG, "onNotificationPosted: SMS")
            val state = spManager.getTurnOnSMS()
            if(state){
                val onTime = spManager.getOnTimeFlashSMSMS()
                val offTime = spManager.getOffTimeFlashSMSMS()
                flashHelper.startNormal(this, onTime, offTime, timeEnd = 5000)
            }
        }
    }
}