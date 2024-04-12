package com.flash.light.component.splash

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.view.View
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
import com.flash.light.admob.CollapsiblePositionType
import com.flash.light.admob.InterAdmob
import com.flash.light.admob.NameRemoteAdmob
import com.flash.light.utils.NativeAdmobUtils
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
        if (spManager.getShowOnBoarding()) {
            if (spManager.getBoolean(NameRemoteAdmob.NATIVE_LANGUAGE, true)) {
                NativeAdmobUtils.loadNativeLanguage()
            }
            if (spManager.getBoolean(NameRemoteAdmob.NATIVE_ONBOARD, true)) {
                NativeAdmobUtils.loadNativeOnboard()
            }
        }

        runProgress()
    }

    private fun runProgress() {

        showBanner()

        if (spManager.getBoolean(NameRemoteAdmob.INTER_SPLASH, true)) {
            val interAdmob = InterAdmob(this@SplashActivity, BuildConfig.inter_splash)
            interAdmob.load(object : BaseAdmob.OnAdmobLoadListener {
                override fun onLoad() {
                    if (spManager.getBoolean(NameRemoteAdmob.INTER_SPLASH, true)) {
                        interAdmob.showInterstitial(
                            this@SplashActivity,
                            object : BaseAdmob.OnAdmobShowListener {
                                override fun onShow() {
                                    gotoMainScreen()
                                }

                                override fun onError(e: String?) {
                                    gotoMainScreen()
                                }
                            })
                    } else {
                        gotoMainScreen()
                    }
                }

                override fun onError(e: String?) {
                    gotoMainScreen()
                }
            })
        } else {
            gotoMainScreen()
        }
    }

    private fun showBanner() {
        if(spManager.getBoolean(NameRemoteAdmob.BANNER_SPLASH, true)){
            val bannerAdmob = BannerAdmob(this, CollapsiblePositionType.NONE)
            bannerAdmob.showBanner(this@SplashActivity, BuildConfig.banner_splash, viewBinding.banner)
        }else{
            viewBinding.banner.visibility = View.GONE
        }
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