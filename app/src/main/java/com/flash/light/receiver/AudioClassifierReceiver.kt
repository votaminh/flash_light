package com.flash.light.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat

class AudioClassifierReceiver : BroadcastReceiver() {
    private val isRegister = false

    var onReceive: ((Boolean) -> Unit)? = null

    companion object {
        private const val ACTION = "com.vinalinux.findphone.action.AUTO_AUDIO_CLASSIFIER_ENABLE"
        private const val EXTRA_AUDIO_CLASSIFIER_ENABLE = "EXTRA_AUDIO_CLASSIFIER_ENABLE"

        fun sendBroadCast(context: Context, isRunningAudioClassifier: Boolean) {
            Intent().also {
                it.action = ACTION
                it.putExtra(EXTRA_AUDIO_CLASSIFIER_ENABLE, isRunningAudioClassifier)
                context.sendBroadcast(it)
            }
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (ACTION == intent?.action) {
            val isRunningAudioClassifier = intent.getBooleanExtra(EXTRA_AUDIO_CLASSIFIER_ENABLE, false)
            onReceive?.invoke(isRunningAudioClassifier)
        }
    }

    fun register(context: Context) {
        if (!isRegister) {
            ContextCompat.registerReceiver(
                context,
                this,
                IntentFilter(ACTION),
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
        }
    }

    fun unregister(context: Context) {
        if (isRegister) {
            onReceive = null
            context.unregisterReceiver(this)
        }
    }
}