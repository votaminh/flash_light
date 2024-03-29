package com.flash.light.utils

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

    companion object {
        private var mInstance : RemoteConfig? = null

        fun instance(): RemoteConfig {
            if(mInstance == null){
                mInstance = RemoteConfig()
            }
            return mInstance as RemoteConfig
        }

        const val KEY_AD_CONFIG = "ad_config"
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
            putBooleanToSP(remoteConfig, NameRemoteAdmob.INTER_SPLASH)
            putBooleanToSP(remoteConfig, NameRemoteAdmob.INTER_CLICK)
            putBooleanToSP(remoteConfig, NameRemoteAdmob.INTER_APPLY)
            putBooleanToSP(remoteConfig, NameRemoteAdmob.NATIVE_LANGUAGE)
            putBooleanToSP(remoteConfig, NameRemoteAdmob.NATIVE_ONBOARD)
            putBooleanToSP(remoteConfig, NameRemoteAdmob.NATIVE_TUTORIAL)
            putBooleanToSP(remoteConfig, NameRemoteAdmob.NATIVE_SOUND)
            putBooleanToSP(remoteConfig, NameRemoteAdmob.NATIVE_HOME)
            putBooleanToSP(remoteConfig, NameRemoteAdmob.APP_RESUME)
        }
    }

    private fun putBooleanToSP(remoteConfig: FirebaseRemoteConfig, name: String) {
        val spManager = App.instance?.applicationContext?.let { SpManager.getInstance(it) }
        spManager?.putBoolean(name, remoteConfig.getBoolean(name))
    }

}