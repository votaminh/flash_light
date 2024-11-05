package com.flash.msc_light.component.splash

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import com.flash.msc_light.base.activity.BaseActivity
import com.flash.msc_light.component.language.LanguageActivity
import com.flash.msc_light.component.main.MainActivity
import com.flash.msc_light.databinding.ActivitySplashBinding
import com.flash.msc_light.utils.SpManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.flash.msc_light.BuildConfig
import com.flash.msc_light.admob.BaseAdmob
import com.flash.msc_light.admob.BaseAdmob.OnAdmobLoadListener
import com.flash.msc_light.admob.BaseAdmob.OnAdmobShowListener
import com.flash.msc_light.admob.InterAdmob
import com.flash.msc_light.admob.NameRemoteAdmob
import com.flash.msc_light.utils.NativeAdmobUtils
import com.flash.msc_light.utils.NetworkUtil

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
        val interAdmob = InterAdmob(this@SplashActivity, BuildConfig.inter_splash)
        interAdmob.load(object : BaseAdmob.OnAdmobLoadListener {
            override fun onLoad() {
                interAdmob.showInterstitial(
                    this@SplashActivity,
                    object : BaseAdmob.OnAdmobShowListener {
                        override fun onShow() {
                           successAction?.invoke()
                        }

                        override fun onError(e: String?) {
                            failAction?.invoke()
                        }
                    })
            }

            override fun onError(e: String?) {
                failAction?.invoke()
            }
        })
    }

    private fun loadShowOpenAds(successAction : (() -> Unit)? = null, failAction : (() -> Unit)? = null) {
        val interAdmob = InterAdmob(this, BuildConfig.inter_splash_high)
        interAdmob.load(object : OnAdmobLoadListener{
            override fun onLoad() {
                interAdmob.showInterstitial(this@SplashActivity, object : OnAdmobShowListener{
                    override fun onShow() {
                        successAction?.invoke()
                    }

                    override fun onError(e: String?) {
                        failAction?.invoke()
                    }

                })
            }

            override fun onError(e: String?) {
                failAction?.invoke()
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
//        if (spManager.getShowOnBoarding()) {
//            LanguageActivity.start(this, true)
//        } else {
//            startActivity(Intent(this, MainActivity::class.java))
//        }
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}