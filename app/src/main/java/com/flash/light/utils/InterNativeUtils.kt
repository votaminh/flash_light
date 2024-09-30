package com.flash.light.utils

import android.app.Activity
import com.flash.light.App
import com.flash.light.BuildConfig
import com.flash.light.admob.BaseAdmob
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
                        BuildConfig.native_permission
                    )
                    interBack?.load(null)
                }
            }
        }

        fun showInterAction(activity : Activity, nextAction : (() -> Unit)? = null){
            if(latestInterShow == 0L){
                latestInterShow = System.currentTimeMillis()
            }else if(System.currentTimeMillis() - latestInterShow < 30000){
                nextAction?.invoke()
                return
            }

            latestInterShow = System.currentTimeMillis()

            if(firstRequest){
                firstRequest = false
                nextAction?.invoke()
                return
            }

            if(interBack == null || !SpManager.getInstance(activity).getBoolean(NameRemoteAdmob.inter_back, true)){
                nextAction?.invoke()
            }else{
                interBack?.showInterstitial(activity, object : BaseAdmob.OnAdmobShowListener{
                    override fun onShow() {
                        nextAction?.invoke()
                        interBack?.load(null)
                    }

                    override fun onError(e: String?) {
                        nextAction?.invoke()
                        interBack?.load(null)
                    }

                })
            }
        }
    }
}