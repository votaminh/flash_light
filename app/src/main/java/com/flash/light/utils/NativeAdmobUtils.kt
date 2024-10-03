package com.flash.light.utils

import android.annotation.SuppressLint
import com.facebook.appevents.AppEventsLogger
import com.flash.light.App
import com.flash.light.BuildConfig
import com.flash.light.admob.BaseAdmob.OnAdmobLoadListener
import com.flash.light.admob.BaseAdmob.OnAdmobShowListener
import com.flash.light.admob.NameRemoteAdmob
import com.flash.light.admob.NativeAdmob

class NativeAdmobUtils {
    companion object {

        @SuppressLint("StaticFieldLeak")
        var languageNative1: NativeAdmob? = null
        @SuppressLint("StaticFieldLeak")
        var languageNative2: NativeAdmob? = null

        @SuppressLint("StaticFieldLeak")
        var onboardNativeAdmob1: NativeAdmob? = null
        @SuppressLint("StaticFieldLeak")
        var onboardNativeAdmob2: NativeAdmob? = null

        var permissionNative : NativeAdmob? = null

        @SuppressLint("StaticFieldLeak")
        var nativeExitLiveData: NativeAdmob? = null

        fun loadNativeLanguage() {
            if(NetworkUtil.isOnline){
                App.instance?.applicationContext?.let { context ->

                    val first = SpManager.getInstance(context).getBoolean("first_native_language", true)
                    if(first){

                        SpManager.getInstance(context).putBoolean("first_native_language", false)

                        languageNative1 = NativeAdmob(
                            context,
                            BuildConfig.native_language_1_1
                        )
                        AppEventsLogger.newLogger(context).logEvent("native_language_1_1_load")
                        languageNative1?.load(object : OnAdmobLoadListener{
                            override fun onLoad() {
                                AppEventsLogger.newLogger(context).logEvent("native_language_1_1_load_success")
                            }

                            override fun onError(e: String?) {
                                AppEventsLogger.newLogger(context).logEvent("native_language_1_1_load_error")
                            }

                        })

                        languageNative2 = NativeAdmob(
                            context,
                            BuildConfig.native_language_1_2
                        )
                        AppEventsLogger.newLogger(context).logEvent("native_language_1_2_load")
                        languageNative2?.load(object : OnAdmobLoadListener{
                            override fun onLoad() {
                                AppEventsLogger.newLogger(context).logEvent("native_language_1_2_load_success")
                            }

                            override fun onError(e: String?) {
                                AppEventsLogger.newLogger(context).logEvent("native_language_1_2_load_fail")
                            }

                        })
                    }else {
                        languageNative1 = NativeAdmob(
                            context,
                            BuildConfig.native_language_2_1
                        )
                        AppEventsLogger.newLogger(context).logEvent("native_language_2_1_load")
                        languageNative1?.load(object : OnAdmobLoadListener{
                            override fun onLoad() {
                                AppEventsLogger.newLogger(context).logEvent("native_language_2_1_load_success")
                            }

                            override fun onError(e: String?) {
                                AppEventsLogger.newLogger(context).logEvent("native_language_2_1_load_fail")
                            }

                        })

                        languageNative2 = NativeAdmob(
                            context,
                            BuildConfig.native_language_2_2
                        )
                        AppEventsLogger.newLogger(context).logEvent("native_language_2_2_load")
                        languageNative2?.load(object : OnAdmobLoadListener{
                            override fun onLoad() {
                                AppEventsLogger.newLogger(context).logEvent("native_language_2_2_load_success")
                            }

                            override fun onError(e: String?) {
                                AppEventsLogger.newLogger(context).logEvent("native_language_2_2_load_fail")
                            }
                        })
                    }
                }
            }
        }

        fun loadNativeOnboard() {
            if(NetworkUtil.isOnline){
                App.instance?.applicationContext?.let {context ->
                    if(SpManager.getInstance(context).getBoolean(NameRemoteAdmob.native_onboarding, true)){

                        val first = SpManager.getInstance(context).getBoolean("first_native_onboarding", true)
                        if(first) {
                            SpManager.getInstance(context).putBoolean("first_native_onboarding", false)
                            onboardNativeAdmob1 = NativeAdmob(
                                context,
                                BuildConfig.native_onboarding_1_1
                            )

                            AppEventsLogger.newLogger(context).logEvent("native_onboarding_1_1_load")
                            onboardNativeAdmob1?.load(object : OnAdmobLoadListener{
                                override fun onLoad() {
                                    AppEventsLogger.newLogger(context).logEvent("native_onboarding_1_1_load_success")
                                }

                                override fun onError(e: String?) {
                                    AppEventsLogger.newLogger(context).logEvent("native_onboarding_1_1_load_fail")
                                }

                            })

                            onboardNativeAdmob2 = NativeAdmob(
                                context,
                                BuildConfig.native_onboarding_1_2
                            )
                            AppEventsLogger.newLogger(context).logEvent("native_onboarding_1_2_load")
                            onboardNativeAdmob2?.load(object : OnAdmobLoadListener{
                                override fun onLoad() {
                                    AppEventsLogger.newLogger(context).logEvent("native_onboarding_1_2_load_success")
                                }

                                override fun onError(e: String?) {
                                    AppEventsLogger.newLogger(context).logEvent("native_onboarding_1_2_load_fail")
                                }

                            })
                        }else{
                            onboardNativeAdmob1 = NativeAdmob(
                                context,
                                BuildConfig.native_onboarding_2_1
                            )
                            AppEventsLogger.newLogger(context).logEvent("native_onboarding_2_1_load")
                            onboardNativeAdmob1?.load(object : OnAdmobLoadListener{
                                override fun onLoad() {
                                    AppEventsLogger.newLogger(context).logEvent("native_onboarding_2_1_load_success")
                                }

                                override fun onError(e: String?) {
                                    AppEventsLogger.newLogger(context).logEvent("native_onboarding_2_1_load_fail")
                                }

                            })

                            onboardNativeAdmob2 = NativeAdmob(
                                context,
                                BuildConfig.native_onboarding_2_2
                            )
                            AppEventsLogger.newLogger(context).logEvent("native_onboarding_2_2_load")
                            onboardNativeAdmob2?.load(object : OnAdmobLoadListener{
                                override fun onLoad() {
                                    AppEventsLogger.newLogger(context).logEvent("native_onboarding_2_2_load_success")
                                }

                                override fun onError(e: String?) {
                                    AppEventsLogger.newLogger(context).logEvent("native_onboarding_2_2_load_error")
                                }

                            })
                        }

                    }
                }
            }
        }

        fun loadNativePermission(){
            if(NetworkUtil.isOnline){
                App.instance?.applicationContext?.let {context ->
                    permissionNative = NativeAdmob(
                        context,
                        BuildConfig.native_permission
                    )
                    AppEventsLogger.newLogger(context).logEvent("native_permission_load")
                    permissionNative?.load(object : OnAdmobLoadListener {
                        override fun onLoad() {
                            AppEventsLogger.newLogger(context).logEvent("native_permission_load_success")
                        }

                        override fun onError(e: String?) {
                            AppEventsLogger.newLogger(context).logEvent("native_permission_load_fail")
                        }

                    })
                }
            }
        }

        fun loadNativeExit(){
//            App.instance?.applicationContext?.let {context ->
//                nativeExitLiveData = NativeAdmob(
//                    context,
//                    BuildConfig.native_exit
//                )
//                nativeExitLiveData?.load(null)
//            }
        }
    }
}