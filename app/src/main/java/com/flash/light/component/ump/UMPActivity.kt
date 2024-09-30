package com.flash.light.component.ump

import com.flash.light.App
import com.flash.light.base.activity.BaseActivity
import com.flash.light.component.splash.SplashActivity
import com.flash.light.databinding.ActivityUmpBinding
import com.flash.light.utils.RemoteConfig
import com.flash.light.utils.SpManager
import com.flash.light.utils.UMPUtils


class UMPActivity : BaseActivity<ActivityUmpBinding>() {
    private val TAG = "ump_activity"

    override fun provideViewBinding(): ActivityUmpBinding = ActivityUmpBinding.inflate(layoutInflater)

    override fun initData() {
        super.initData()

        RemoteConfig.instance().fetch()
        openSplash();
//
//        if(SpManager.getInstance(this).isUMPShowed()){
//            RemoteConfig.instance().fetch()
//            openSplash();
//        }else{
//            RemoteConfig.instance().fetch{
//                initUmp()
//            }
//        }
    }

    private fun openSplash() {

        val app : App = application as App
        app.initAds()

        SpManager.getInstance(this).setUMPShowed(true)
        SplashActivity.start(this);
        finish()
    }

    private fun initUmp() {
        UMPUtils.init(this@UMPActivity, nextAction = {
            openSplash()
        }, true, false)
    }
}