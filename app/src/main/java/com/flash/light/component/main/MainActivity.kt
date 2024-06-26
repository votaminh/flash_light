package com.flash.light.component.main

import android.app.Activity
import android.app.ActivityManager
import android.content.Intent
import android.provider.Settings
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import com.flash.light.App
import com.flash.light.BuildConfig
import com.flash.light.R
import com.flash.light.admob.BannerAdmob
import com.flash.light.admob.BaseAdmob
import com.flash.light.admob.CollapsiblePositionType
import com.flash.light.admob.InterAdmob
import com.flash.light.admob.NameRemoteAdmob
import com.flash.light.base.activity.BaseActivity
import com.flash.light.databinding.ActivityMainBinding
import com.flash.light.dialog.DialogExt.showDialogExit
import com.flash.light.service.PhoneCallComingService
import com.flash.light.utils.NativeAdmobUtils
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

    private var interAdmob : InterAdmob? = null

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
                checkShowInter()
            }
            light.setOnClickListener {
                viewPager2.currentItem = 1
                resetAllMenu()
                imvLight.changeTint(R.color.main)
                tvLight.changeTextColor(R.color.main)
                checkShowInter()
            }
            blinks.setOnClickListener {
                viewPager2.currentItem = 2
                resetAllMenu()
                imvBlinks.changeTint(R.color.main)
                tvBlinks.changeTextColor(R.color.main)
                checkShowInter()
            }
            settings.setOnClickListener {
                viewPager2.currentItem = 3
                resetAllMenu()
                imvSetting.changeTint(R.color.main)
                tvSetting.changeTextColor(R.color.main)
                checkShowInter()
            }

        }

        startNotificationFlashService()
        showBanner()

        interAdmob = InterAdmob(this@MainActivity, BuildConfig.inter_home)
        loadInter()
        if(spManager.getBoolean(NameRemoteAdmob.NATIVE_EXIT, true)){
            NativeAdmobUtils.loadNativeExit()
        }
        
        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(spManager.getBoolean(NameRemoteAdmob.NATIVE_EXIT, true)){
                    showDialogExit(this@MainActivity){
                        finish()
                    }
                }else{
                    finish()
                }
            }
        })

        checkNotificationPermisison()
    }

    private fun checkNotificationPermisison() {
        if(!PermissionUtils.permissionNotification(this)){
            PermissionUtils.requestNotificationPermission(this, 322)
        }
    }

    override fun onBackPressed() {
        if(spManager.getBoolean(NameRemoteAdmob.NATIVE_EXIT, true)){
            showDialogExit(this@MainActivity){
                finish()
            }
        }else{
            finish()
        }
    }

    private fun checkShowInter() {
        interAdmob?.run {
            if(available() && spManager.getBoolean(NameRemoteAdmob.INTER_HOME, true)){
                showInterstitial(this@MainActivity, object : BaseAdmob.OnAdmobShowListener{
                    override fun onShow() {
                        loadInter()
                    }

                    override fun onError(e: String?) {
                        loadInter()
                    }
                })
            }
        }
    }

    private fun loadInter() {
        if(spManager.getBoolean(NameRemoteAdmob.INTER_HOME, true)){
            interAdmob?.load(null)
        }
    }

    private fun showBanner() {
        if(spManager.getBoolean(NameRemoteAdmob.BANNER_ALL, true)){
            val bannerAdmob = BannerAdmob(this, CollapsiblePositionType.NONE)
            bannerAdmob.showBanner(this@MainActivity, BuildConfig.banner_all, viewBinding.banner)
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