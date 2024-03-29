package com.vinalinux.findphone.component.ump

import com.vinalinux.findphone.App
import com.vinalinux.findphone.base.activity.BaseActivity
import com.vinalinux.findphone.component.splash.SplashActivity
import com.vinalinux.findphone.databinding.ActivityUmpBinding
import com.vinalinux.findphone.utils.RemoteConfig
import com.vinalinux.findphone.utils.SpManager
import com.vinalinux.findphone.utils.UMPUtils


class UMPActivity : BaseActivity<ActivityUmpBinding>() {
    private val TAG = "ump_activity"

    override fun provideViewBinding(): ActivityUmpBinding = ActivityUmpBinding.inflate(layoutInflater)

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