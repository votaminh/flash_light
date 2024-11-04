package com.flash.light.service

import android.telecom.Call
import android.telecom.InCallService
import android.util.Log

class PhoneCallCloneMscService : InCallService() {
    private val callBack = object : Call.Callback() {
        override fun onStateChanged(call: Call?, state: Int) {
            Log.i("gsdgdsg", "onStateChanged: ")
        }

        override fun onDetailsChanged(call: Call?, details: Call.Details?) {
            Log.i("gsdgdsg", "onDetailsChanged: ")
        }
    }
    override fun onCallAdded(call: Call?) {
        super.onCallAdded(call)
        call?.registerCallback(callBack)
        Log.i("gsdgdsg", "onCallAdded: ")
    }

    override fun onCallRemoved(call: Call?) {
        super.onCallRemoved(call)
        call?.unregisterCallback(callBack)
        Log.i("gsdgdsg", "onCallRemoved: ")
    }

}