package com.flash.light.service

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log

class PhoneCallComingService : Service() {

    companion object {
        const val TAG = "PhoneCallComingService"
    }

    private val phoneStateListener: PhoneStateListener? = null
    private var receiver: BroadcastReceiver? = null
    private val telephonyManager: TelephonyManager? = null

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, i: Int, i2: Int): Int {
        regis()
        return super.onStartCommand(intent, i, i2)
    }

    private fun regis() {
        Log.i(TAG, "regis: ")
        val intentFilter = IntentFilter()
        intentFilter.addAction("android.intent.action.PHONE_STATE")
        val myReceiver: BroadcastReceiver = MyReceiver()
        this.receiver = myReceiver
        registerReceiver(myReceiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy: ")
        val broadcastReceiver = receiver
        broadcastReceiver?.let { unregisterReceiver(it) }
    }

    class MyReceiver() : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.i(TAG, "onReceive: ${intent.extras?.getString("state")}")
        }
    }
}