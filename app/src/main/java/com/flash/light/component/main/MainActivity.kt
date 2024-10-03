package com.flash.light.component.main

import android.app.Activity
import android.app.ActivityManager
import android.content.Intent
import android.provider.Settings
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import com.facebook.appevents.AppEventsLogger
import com.flash.light.App
import com.flash.light.BuildConfig
import com.flash.light.R
import com.flash.light.admob.BannerAdmob
import com.flash.light.admob.BaseAdmob
import com.flash.light.admob.BaseAdmob.OnAdmobLoadListener
import com.flash.light.admob.CollapsiblePositionType
import com.flash.light.admob.InterAdmob
import com.flash.light.admob.NameRemoteAdmob
import com.flash.light.base.activity.BaseActivity
import com.flash.light.component.alert.FlashAlertFragment
import com.flash.light.databinding.ActivityMainBinding
import com.flash.light.dialog.DialogExt.showDialogExit
import com.flash.light.service.PhoneCallComingService
import com.flash.light.utils.InterNativeUtils
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

    private val viewPagerAdapter = ViewPagerAdapter(this@MainActivity)

    private var latestInterShow: Long = 0
    private var firstRequest = true
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
        buildPagerHome()

        viewBinding.run {
            alert.setOnClickListener {
                showInterAction {
                    viewPager2.currentItem = 0
                    resetAllMenu()
                    imvAlert.changeTint(R.color.main)
                    tvAlert.changeTextColor(R.color.main)
                    if(viewPagerAdapter.fragments.get(0) is FlashAlertFragment){
                        (viewPagerAdapter.fragments.get(0) as FlashAlertFragment).showNativeHome()
                    }
                }
            }
            light.setOnClickListener {
                showInterAction {
                    viewPager2.currentItem = 1
                    resetAllMenu()
                    imvLight.changeTint(R.color.main)
                    tvLight.changeTextColor(R.color.main)
                }
            }
            blinks.setOnClickListener {
                showInterAction {
                    viewPager2.currentItem = 2
                    resetAllMenu()
                    imvBlinks.changeTint(R.color.main)
                    tvBlinks.changeTextColor(R.color.main)
                }
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
        loadInter()
        InterNativeUtils.loadInterBack()
        checkNotificationPermisison()
    }

    private fun checkNotificationPermisison() {
        if(!PermissionUtils.permissionNotification(this)){
            PermissionUtils.requestNotificationPermission(this, 322)
        }
    }


    private fun loadInter() {
        if(spManager.getBoolean(NameRemoteAdmob.inter_home, true)){
            AppEventsLogger.newLogger(this@MainActivity).logEvent("inter_home_load")
            interAdmob = InterAdmob(this, BuildConfig.inter_home)
            interAdmob?.load(object : OnAdmobLoadListener{
                override fun onLoad() {
                    AppEventsLogger.newLogger(this@MainActivity).logEvent("inter_home_load_success")
                }

                override fun onError(e: String?) {
                    AppEventsLogger.newLogger(this@MainActivity).logEvent("inter_home_load_fail")
                }
            })
        }
    }

    fun showInterAction(nextAction : (() -> Unit)? = null){
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

        if(interAdmob == null || !spManager.getBoolean(NameRemoteAdmob.inter_home, true)){
            nextAction?.invoke()
        }else{
            interAdmob?.showInterstitial(this, object : BaseAdmob.OnAdmobShowListener{
                override fun onShow() {
                    AppEventsLogger.newLogger(this@MainActivity).logEvent("inter_home_show_success")
                    nextAction?.invoke()
                    loadInter()
                }

                override fun onError(e: String?) {
                    nextAction?.invoke()
                    AppEventsLogger.newLogger(this@MainActivity).logEvent("inter_home_show_fail")
                    loadInter()
                }

            })
        }
    }

    private fun showBanner() {
        if(spManager.getBoolean(NameRemoteAdmob.banner_home, true)){
            val bannerAdmob = BannerAdmob(this, CollapsiblePositionType.BOTTOM)
            AppEventsLogger.newLogger(this@MainActivity).logEvent("banner_home_load")
            bannerAdmob.showBanner(this@MainActivity, BuildConfig.banner_home, viewBinding.banner, object : OnAdmobLoadListener{
                override fun onLoad() {
                    AppEventsLogger.newLogger(this@MainActivity).logEvent("banner_home_load_success")
                }

                override fun onError(e: String?) {
                    AppEventsLogger.newLogger(this@MainActivity).logEvent("banner_home_load_fail")
                }
            })
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
            adapter = viewPagerAdapter
        }
    }
}