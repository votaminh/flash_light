package com.flash.light.utils

import android.app.Activity
import com.flash.light.App
import com.flash.light.BuildConfig
import com.flash.light.admob.BaseAdmob
import com.flash.light.admob.BaseAdmob.OnAdmobLoadListener
import com.flash.light.admob.InterAdmob
import com.flash.light.admob.NameRemoteAdmob
import com.flash.light.admob.NativeAdmob
import com.flash.light.utils.NativeAdmobUtils.Companion.permissionNative

class InterNativeUtils {
    companion object {
        var interBack : InterAdmob? = null
        private var latestInterShow: Long = 0
        private var firstRequest = true

        fun loadInterBack(){
            App.instance?.applicationContext?.let { context ->
                if(SpManager.getInstance(context).getBoolean(NameRemoteAdmob.inter_back, true)){
                    interBack = InterAdmob(
                        context,
                        BuildConfig.inter_back
                    )
                    interBack?.load(object : OnAdmobLoadListener{
                        override fun onLoad() {
                        }

                        override fun onError(e: String?) {
                        }

                    })
                }
            }
        }

        fun showInterAction(activity : Activity, nextAction : (() -> Unit)? = null){
            if(firstRequest){
                firstRequest = false
                nextAction?.invoke()
                return
            }

            if(latestInterShow == 0L){
                latestInterShow = System.currentTimeMillis()
            }else if(System.currentTimeMillis() - latestInterShow < 30000){
                nextAction?.invoke()
                return
            }

            latestInterShow = System.currentTimeMillis()

            if(interBack == null || !SpManager.getInstance(activity).getBoolean(NameRemoteAdmob.inter_back, true)){
                nextAction?.invoke()
            }else{
                interBack?.showInterstitial(activity, object : BaseAdmob.OnAdmobShowListener{
                    override fun onShow() {
                        nextAction?.invoke()
                        loadInterBack()
                    }

                    override fun onError(e: String?) {
                        nextAction?.invoke()
                        loadInterBack()
                    }

                })
            }
        }
    }
}