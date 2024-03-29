package com.vinalinux.findphone.utils

import android.annotation.SuppressLint
import com.vinalinux.findphone.App
import com.vinalinux.findphone.BuildConfig
import com.vinalinux.findphone.admob.NativeAdmob

class NativeAdmobUtils {
    companion object {
        @SuppressLint("StaticFieldLeak")
        var languageNativeAdmob: NativeAdmob? = null

        @SuppressLint("StaticFieldLeak")
        var onboardNativeAdmob: NativeAdmob? = null

        @SuppressLint("StaticFieldLeak")
        var homeNativeAdmob: NativeAdmob? = null

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

        fun loadNativeHome(){
            App.instance?.applicationContext?.let {context ->
                homeNativeAdmob = NativeAdmob(
                    context,
                    BuildConfig.native_home
                )
                homeNativeAdmob?.load(null)
            }
        }
    }
}