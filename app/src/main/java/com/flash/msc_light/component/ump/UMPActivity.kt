package com.flash.msc_light.component.ump

import com.flash.msc_light.App
import com.flash.msc_light.base.activity.BaseActivity
import com.flash.msc_light.component.splash.SplashActivity
import com.flash.msc_light.databinding.ActivitySplashBinding
import com.flash.msc_light.databinding.ActivityUmpBinding
import com.flash.msc_light.utils.RemoteConfig
import com.flash.msc_light.utils.SpManager
import com.flash.msc_light.utils.UMPUtils


class UMPActivity : BaseActivity<ActivitySplashBinding>() {
    private val TAG = "ump_activity"

    override fun provideViewBinding(): ActivitySplashBinding = ActivitySplashBinding.inflate(layoutInflater)

    override fun initData() {
        super.initData()

        if(SpManager.getInstance(this).isUMPShowed()){
            RemoteConfig.instance().fetch()
            openSplash();
        }else{
            RemoteConfig.instance().fetch{
                initUmp()
            }
        }
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