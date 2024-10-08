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
import com.flash.light.admob.BaseAdmob.OnAdmobLoadListener
import com.flash.light.admob.BaseAdmob.OnAdmobShowListener
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
import com.vungle.ads.VunglePrivacySettings
import dagger.hilt.android.HiltAndroidApp
import org.json.JSONException
import org.json.JSONObject
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
        MobileAds.initialize(this)
        val requestConfiguration = RequestConfiguration.Builder().build()
        MobileAds.setRequestConfiguration(requestConfiguration)

        if(spManager.getBoolean(NameRemoteAdmob.open_resume, true)){
            openAdmob = OpenAdmob(this, BuildConfig.open_resume)
            loadOpenResume()
        }

        initMediation()
    }

    private fun initMediation() {
        initPangle()
        initVungle()
        initApplovin()
        initFAN()
        initMintegral()
        initInMobi()
        initIronSource()
    }

    private fun initFAN() {
        // no request code
    }

    private fun initVungle() {
        VunglePrivacySettings.setGDPRStatus(true, "v1.0.0");
    }

    private fun initPangle() {
        // no request code
    }

    private fun initApplovin(){
        AppLovinPrivacySettings.setDoNotSell(true, this);
        VunglePrivacySettings.setCCPAStatus(true);
    }

    private fun initInMobi(){
//        val consentObject = JSONObject()
//        try {
//            consentObject.put(InMobiSdk.IM_GDPR_CONSENT_AVAILABLE, true)
//            consentObject.put("gdpr", "1")
//        } catch (exception: JSONException) {
//            exception.printStackTrace()
//        }
//
//        InMobiConsent.updateGDPRConsent(consentObject)
    }

    private fun initMintegral(){
        val sdk = MBridgeSDKFactory.getMBridgeSDK()
        sdk.setConsentStatus(this, MBridgeConstans.IS_SWITCH_ON)
    }

    private fun initIronSource(){
//        IronSource.setConsent(true);
//        IronSource.setMetaData("do_not_sell", "true")
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        if(spManager.getBoolean(NameRemoteAdmob.open_resume, true)){
            openAdmob?.run {
                currentActivity?.let { showAdIfAvailable(it, object : OnAdmobShowListener{
                    override fun onShow() {
                        loadOpenResume()
                    }

                    override fun onError(e: String?) {
                        loadOpenResume()
                    }
                }) }
            }
        }
    }

    private fun loadOpenResume() {
        currentActivity?.let { currentActivity ->
            openAdmob?.loadAd(currentActivity, object : OnAdmobLoadListener{
                override fun onLoad() {
                }

                override fun onError(e: String?) {
                }

            })
        }
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