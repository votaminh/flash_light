package com.flash.light.utils

import android.annotation.SuppressLint
import com.flash.light.App
import com.flash.light.BuildConfig
import com.flash.light.admob.NativeAdmob

class NativeAdmobUtils {
    companion object {
        @SuppressLint("StaticFieldLeak")
        var languageNativeAdmob: NativeAdmob? = null

        @SuppressLint("StaticFieldLeak")
        var onboardNativeAdmob: NativeAdmob? = null

        @SuppressLint("StaticFieldLeak")
        var nativeExitLiveData: NativeAdmob? = null

        fun loadNativeLanguage() {
            App.instance?.applicationContext?.let {context ->
                languageNativeAdmob = NativeAdmob(
                    context,
                    BuildConfig.native_language
                )
                languageNativeAdmob?.load(null)
            }
        }

        fun loadNativeOnboard() {
            App.instance?.applicationContext?.let {context ->
                onboardNativeAdmob = NativeAdmob(
                    context,
                    BuildConfig.native_onboarding
                )
                onboardNativeAdmob?.load(null)
            }
        }

        fun loadNativeExit(){
            App.instance?.applicationContext?.let {context ->
                nativeExitLiveData = NativeAdmob(
                    context,
                    BuildConfig.native_exit
                )
                nativeExitLiveData?.load(null)
            }
        }
    }
}