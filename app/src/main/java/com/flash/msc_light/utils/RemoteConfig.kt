package com.flash.msc_light.utils

import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.flash.msc_light.App
import com.flash.msc_light.BuildConfig
import org.json.JSONObject

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
            val spManager = App.instance?.applicationContext?.let { SpManager.getInstance(it) }

            val remoteConfig = Firebase.remoteConfig

            val baseConfigString = remoteConfig.getString("base_inv_config")

            Log.i(TAG, "updateConfig: " + baseConfigString)

            val baseConfigJson = JSONObject(baseConfigString)
            val showAdsAdmob = baseConfigJson.getBoolean("show_ads_admob")

            spManager?.putBoolean(SpManager.can_show_ads, showAdsAdmob)

            // neu da tat ads thi toan bo version deu tat k check them
            if(!showAdsAdmob){
                return
            }

            // neu k tat ads all thi check tat ads theo version cho che do review
            val disableForReviewJson = baseConfigJson.getJSONObject("disable_to_reivew")
            val enable = disableForReviewJson.getBoolean("enable")
            val version = disableForReviewJson.getInt("version")

            if(enable && version == BuildConfig.VERSION_CODE){
                spManager?.putBoolean(SpManager.can_show_ads, false)
                return
            }
        }
    }

}