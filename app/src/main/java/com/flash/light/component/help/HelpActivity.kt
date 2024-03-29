package com.flash.light.component.help

import android.view.View
import com.flash.light.BuildConfig
import com.flash.light.admob.BaseAdmob
import com.flash.light.admob.NameRemoteAdmob
import com.flash.light.admob.NativeAdmob
import com.flash.light.base.activity.BaseActivity
import com.flash.light.databinding.ActivityHelpBinding
import com.flash.light.utils.SpManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HelpActivity : BaseActivity<ActivityHelpBinding>() {

    @Inject
    lateinit var spManager: SpManager

    override fun provideViewBinding(): ActivityHelpBinding = ActivityHelpBinding.inflate(layoutInflater)

    override fun initViews() {
        viewBinding.ivBack.setOnClickListener { finish() }

        if(spManager.getBoolean(NameRemoteAdmob.NATIVE_TUTORIAL, true)){
            val nativeAdmob = NativeAdmob(this@HelpActivity, BuildConfig.native_tutorial)
            nativeAdmob.run {
                load(object : BaseAdmob.OnAdmobLoadListener{
                    override fun onLoad() {
                        nativeAdLive.observe(this@HelpActivity){
                            if(available() && spManager.getBoolean(NameRemoteAdmob.NATIVE_TUTORIAL, true)){
                                nativeAdmob.showNative(
                                    viewBinding.flAdplaceholder, null
                                )
                            }
                        }
                    }

                    override fun onError(e: String?) {
                        viewBinding.flAdplaceholder.visibility = View.GONE
                    }

                })
            }
        }else{
            viewBinding.flAdplaceholder.visibility = View.GONE
        }
    }
}