package com.flash.light.utils

import android.annotation.SuppressLint
import com.flash.light.App
import com.flash.light.BuildConfig
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
                        languageNative1?.load(null)

                        languageNative2 = NativeAdmob(
                            context,
                            BuildConfig.native_language_1_2
                        )
                        languageNative2?.load(null)
                    }else {
                        languageNative1 = NativeAdmob(
                            context,
                            BuildConfig.native_language_2_1
                        )
                        languageNative1?.load(null)

                        languageNative2 = NativeAdmob(
                            context,
                            BuildConfig.native_language_2_2
                        )
                        languageNative2?.load(null)
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
                            onboardNativeAdmob1?.load(null)

                            onboardNativeAdmob2 = NativeAdmob(
                                context,
                                BuildConfig.native_onboarding_1_2
                            )
                            onboardNativeAdmob2?.load(null)
                        }else{
                            onboardNativeAdmob1 = NativeAdmob(
                                context,
                                BuildConfig.native_onboarding_2_1
                            )
                            onboardNativeAdmob1?.load(null)

                            onboardNativeAdmob2 = NativeAdmob(
                                context,
                                BuildConfig.native_onboarding_2_1
                            )
                            onboardNativeAdmob2?.load(null)
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
                    permissionNative?.load(null)
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