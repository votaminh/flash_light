package com.flash.light.component.main

import android.app.Activity
import android.app.ActivityManager
import android.content.Intent
import android.provider.Settings
import android.view.View
import androidx.core.content.ContextCompat
import com.flash.light.BuildConfig
import com.flash.light.R
import com.flash.light.admob.BannerAdmob
import com.flash.light.admob.CollapsiblePositionType
import com.flash.light.admob.NameRemoteAdmob
import com.flash.light.base.activity.BaseActivity
import com.flash.light.databinding.ActivityMainBinding
import com.flash.light.service.PhoneCallComingService
import com.flash.light.utils.PermissionUtils
import com.flash.light.utils.SpManager
import com.flash.light.utils.changeTextColor
import com.flash.light.utils.changeTint
import com.flash.light.utils.startNotificationFlashService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.jvm.internal.Intrinsics

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    @Inject
    lateinit var spManager: SpManager

    companion object {
        fun start(activity : Activity){
            activity.startActivity(Intent(activity, MainActivity::class.java))
        }
    }


    override fun provideViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        spManager.saveOnBoarding()
        buildPagerHome()

        viewBinding.run {
            alert.setOnClickListener {
                viewPager2.currentItem = 0
                resetAllMenu()
                imvAlert.changeTint(R.color.main)
                tvAlert.changeTextColor(R.color.main)
            }
            light.setOnClickListener {
                viewPager2.currentItem = 1
                resetAllMenu()
                imvLight.changeTint(R.color.main)
                tvLight.changeTextColor(R.color.main)
            }
            blinks.setOnClickListener {
                viewPager2.currentItem = 2
                resetAllMenu()
                imvBlinks.changeTint(R.color.main)
                tvBlinks.changeTextColor(R.color.main)
            }
            settings.setOnClickListener {
                viewPager2.currentItem = 3
                resetAllMenu()
                imvSetting.changeTint(R.color.main)
                tvSetting.changeTextColor(R.color.main)
            }

        }

        startNotificationFlashService()
        showBanner()
    }

    private fun showBanner() {
        if(spManager.getBoolean(NameRemoteAdmob.BANNER_ALL, true)){
            val bannerAdmob = BannerAdmob(this, CollapsiblePositionType.NONE)
            bannerAdmob.showBanner(this@MainActivity, BuildConfig.banner_splash, viewBinding.banner)
        }else{
            viewBinding.banner.visibility = View.GONE
        }
    }

    private fun resetAllMenu() {
        viewBinding.run {
            imvAlert.changeTint(R.color.white)
            tvAlert.changeTextColor(R.color.white)
            imvLight.changeTint(R.color.white)
            tvLight.changeTextColor(R.color.white)
            imvBlinks.changeTint(R.color.white)
            tvBlinks.changeTextColor(R.color.white)
            imvSetting.changeTint(R.color.white)
            tvSetting.changeTextColor(R.color.white)
        }
    }

    private fun buildPagerHome() {
        viewBinding.viewPager2.run {
            offscreenPageLimit = 4
            isUserInputEnabled = false
            adapter = ViewPagerAdapter(this@MainActivity)
        }
    }
}