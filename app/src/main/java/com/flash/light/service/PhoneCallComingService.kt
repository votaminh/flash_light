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
import com.flash.light.utils.FlashHelper
import com.flash.light.utils.SpManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

class PhoneCallComingService : Service() {

    companion object {
        const val TAG = "PhoneCallComingService"
    }

    private var receiver: BroadcastReceiver? = null

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, i: Int, i2: Int): Int {
        regis()
        return super.onStartCommand(intent, i, i2)
    }

    private fun regis() {
        Log.i(TAG, "regis: ")
        if(receiver != null){
            unregisterReceiver(receiver)
            receiver = null
        }
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

    @AndroidEntryPoint
    class MyReceiver : BroadcastReceiver() {
        private val flashHelper = FlashHelper()
        @Inject
        lateinit var spManager: SpManager

        override fun onReceive(context: Context, intent: Intent) {
            val state = intent.extras?.getString("state")
            Log.i(TAG, "onReceive: $state")
            if(state == "RINGING"){
                val state = spManager.getTurnOnCall()
                if(state){
                    val onTime = spManager.getOnTimeFlashCallMS()
                    val offTime = spManager.getOffTimeFlashCallMS()
                    flashHelper.start(context, onTime, offTime)
                }
            }else{
                flashHelper.stop()
            }
        }
    }
}