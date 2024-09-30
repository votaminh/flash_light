package com.flash.light

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.applovin.sdk.AppLovinPrivacySettings
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.firebase.FirebaseApp
import com.mbridge.msdk.MBridgeConstans
import com.mbridge.msdk.out.MBridgeSDKFactory
import com.flash.light.admob.NameRemoteAdmob
import com.flash.light.admob.OpenAdmob
import com.flash.light.utils.NetworkUtil
import com.flash.light.utils.RemoteConfig
import com.flash.light.utils.SpManager
import dagger.hilt.android.HiltAndroidApp
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@HiltAndroidApp
@Singleton
class App : Application(), Application.ActivityLifecycleCallbacks, DefaultLifecycleObserver {

    companion object {
        var instance: App? = null
    }

    private val TAG = "sdgsdg"

    @Inject
    lateinit var spManager: SpManager

    private var currentActivity: Activity? = null
    private var openAdmob: OpenAdmob? = null

    override fun onCreate() {
        super<Application>.onCreate()
        instance = this

        FirebaseApp.initializeApp(this)
        RemoteConfig.instance().fetch()
        registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        NetworkUtil.initNetwork(this)
    }

    fun initAds() {

        initMediation();

        MobileAds.initialize(this)
        val requestConfiguration = RequestConfiguration.Builder().build()
        MobileAds.setRequestConfiguration(requestConfiguration)

//        if(spManager.getBoolean(NameRemoteAdmob.APP_RESUME, true)){
//            openAdmob = OpenAdmob(this, BuildConfig.open_resume)
//        }
    }

    private fun initMediation() {
        initMintegral()
        initAppLovin()
    }

    private fun initMintegral() {
        val sdk = MBridgeSDKFactory.getMBridgeSDK()
        sdk.setConsentStatus(applicationContext, MBridgeConstans.IS_SWITCH_ON)
        sdk.setDoNotTrackStatus(false)
    }

    private fun initAppLovin() {
        AppLovinPrivacySettings.setHasUserConsent(true, applicationContext)
        AppLovinPrivacySettings.setIsAgeRestrictedUser(true, applicationContext)
        AppLovinPrivacySettings.setDoNotSell(true, applicationContext)
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
//        if(spManager.getBoolean(NameRemoteAdmob.APP_RESUME, true)){
//            openAdmob?.run {
//                currentActivity?.let { showAdIfAvailable(it) }
//            }
//        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {
        Log.i(TAG, "onActivityResumed: ")
    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {

    }


}