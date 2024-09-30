package com.flash.light.utils

import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.flash.light.App
import com.flash.light.admob.NameRemoteAdmob

class RemoteConfig {

    private val TAG = "remoteConfig"

    companion object {
        private var mInstance : RemoteConfig? = null

        fun instance(): RemoteConfig {
            if(mInstance == null){
                mInstance = RemoteConfig()
            }
            return mInstance as RemoteConfig
        }

    }

    fun fetch(success : (() -> Unit)? = null) {
        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig

        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                } else {

                }
                success?.invoke()
                updateConfig()
            }

        remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                remoteConfig.activate().addOnCompleteListener {
                    updateConfig()
                }
            }

            override fun onError(error: FirebaseRemoteConfigException) {

            }
        })
    }

    private fun updateConfig() {
        kotlin.runCatching {
            val remoteConfig = Firebase.remoteConfig
            putBooleanToSP(remoteConfig, NameRemoteAdmob.inter_splash)
            putBooleanToSP(remoteConfig, NameRemoteAdmob.native_language)
            putBooleanToSP(remoteConfig, NameRemoteAdmob.native_onboarding)
            putBooleanToSP(remoteConfig, NameRemoteAdmob.native_permission)
            putBooleanToSP(remoteConfig, NameRemoteAdmob.native_home)
            putBooleanToSP(remoteConfig, NameRemoteAdmob.banner_home)
            putBooleanToSP(remoteConfig, NameRemoteAdmob.inter_home)
            putBooleanToSP(remoteConfig, NameRemoteAdmob.native_function)
        }
    }

    private fun putBooleanToSP(remoteConfig: FirebaseRemoteConfig, name: String) {
        val spManager = App.instance?.applicationContext?.let { SpManager.getInstance(it) }
        val values = remoteConfig.getBoolean(name)
        spManager?.putBoolean(name, values)
        Log.i(TAG, "$name : $values")
    }

}