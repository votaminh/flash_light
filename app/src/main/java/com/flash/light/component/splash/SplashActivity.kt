package com.flash.light.component.splash

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.view.View
import com.facebook.appevents.AppEventsLogger
import com.flash.light.base.activity.BaseActivity
import com.flash.light.component.language.LanguageActivity
import com.flash.light.component.main.MainActivity
import com.flash.light.databinding.ActivitySplashBinding
import com.flash.light.utils.SpManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.flash.light.BuildConfig
import com.flash.light.admob.BannerAdmob
import com.flash.light.admob.BaseAdmob
import com.flash.light.admob.BaseAdmob.OnAdmobShowListener
import com.flash.light.admob.CollapsiblePositionType
import com.flash.light.admob.InterAdmob
import com.flash.light.admob.NameRemoteAdmob
import com.flash.light.admob.OpenAdmob
import com.flash.light.utils.NativeAdmobUtils
import com.flash.light.utils.NetworkUtil
import com.flash.light.utils.RemoteConfig

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    private var progressAnimator: ValueAnimator? = null

    @Inject
    lateinit var spManager: SpManager

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, SplashActivity::class.java))
        }
    }

    override fun provideViewBinding(): ActivitySplashBinding =
        ActivitySplashBinding.inflate(layoutInflater)

    override fun onDestroy() {
        cancelLoadingListener()
        super.onDestroy()
    }

    private fun cancelLoadingListener() {
        progressAnimator?.removeAllListeners()
        progressAnimator?.cancel()
        progressAnimator = null
    }

    override fun onResume() {
        super.onResume()
        if (progressAnimator?.isPaused == true) {
            progressAnimator?.resume()
        }
    }

    override fun onPause() {
        progressAnimator?.pause()
        super.onPause()
    }

    override fun initViews() {
        if (spManager.getShowOnBoarding() && NetworkUtil.isOnline) {
            if (spManager.getBoolean(NameRemoteAdmob.native_language, true)) {
                NativeAdmobUtils.loadNativeLanguage()
            }
        }

        runProgress()
    }

    private fun runProgress() {
        showBanner()
        if (spManager.getBoolean(NameRemoteAdmob.inter_splash, true)) {
            loadShowOpenAds(successAction = {
                gotoMainScreen()
            }, failAction = {
                loadShowInter(successAction = {
                    gotoMainScreen()
                }, failAction = {
                    gotoMainScreen()
                })
            })
        } else {
            gotoMainScreen()
        }
    }

    private fun loadShowInter(successAction : (() -> Unit)? = null, failAction : (() -> Unit)? = null) {
        AppEventsLogger.newLogger(this@SplashActivity).logEvent("inter_splash_load")
        val interAdmob = InterAdmob(this@SplashActivity, BuildConfig.inter_splash)
        interAdmob.load(object : BaseAdmob.OnAdmobLoadListener {
            override fun onLoad() {
                AppEventsLogger.newLogger(this@SplashActivity).logEvent("inter_splash_load_success")
                interAdmob.showInterstitial(
                    this@SplashActivity,
                    object : BaseAdmob.OnAdmobShowListener {
                        override fun onShow() {
                            AppEventsLogger.newLogger(this@SplashActivity).logEvent("inter_splash_show_success")
                           successAction?.invoke()
                        }

                        override fun onError(e: String?) {
                            AppEventsLogger.newLogger(this@SplashActivity).logEvent("inter_splash_show_fail")
                            failAction?.invoke()
                        }
                    })
            }

            override fun onError(e: String?) {
                failAction?.invoke()
                AppEventsLogger.newLogger(this@SplashActivity).logEvent("inter_splash_load_fail")
            }
        })
    }

    private fun loadShowOpenAds(successAction : (() -> Unit)? = null, failAction : (() -> Unit)? = null) {
        AppEventsLogger.newLogger(this).logEvent("open_splash_load")
        val openAdmob = OpenAdmob(this, BuildConfig.open_splash)
        openAdmob.loadAd(this@SplashActivity, object : BaseAdmob.OnAdmobLoadListener {
            override fun onLoad() {
                AppEventsLogger.newLogger(this@SplashActivity).logEvent("open_splash_load_success")
                openAdmob.showAdIfAvailable(this@SplashActivity, object : OnAdmobShowListener{
                    override fun onShow() {
                        AppEventsLogger.newLogger(this@SplashActivity).logEvent("open_splash_show_success")
                        successAction?.invoke()
                    }

                    override fun onError(e: String?) {
                        failAction?.invoke()
                        AppEventsLogger.newLogger(this@SplashActivity).logEvent("open_splash_show_fail")
                    }
                })
            }

            override fun onError(e: String?) {
                failAction?.invoke()
                AppEventsLogger.newLogger(this@SplashActivity).logEvent("open_splash_load_fail")
            }
        })
    }

    private fun showBanner() {
//        if(spManager.getBoolean(NameRemoteAdmob.BANNER_SPLASH, true)){
//            val bannerAdmob = BannerAdmob(this, CollapsiblePositionType.NONE)
//            bannerAdmob.showBanner(this@SplashActivity, BuildConfig.banner_splash, viewBinding.banner)
//        }else{
//            viewBinding.banner.visibility = View.GONE
//        }
    }

    private fun gotoMainScreen() {
        cancelLoadingListener()
        if (spManager.getShowOnBoarding()) {
            LanguageActivity.start(this, true)
        } else {
            startActivity(Intent(this, MainActivity::class.java))
        }
        finish()
    }
}