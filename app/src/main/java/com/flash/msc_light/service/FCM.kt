package com.flash.msc_light.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class FCM : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        Log.i("gsdgdsg", "onNewToken: " + token)
        super.onNewToken(token)
    }
}