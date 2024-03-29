package com.vinalinux.findphone.component.splash

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import com.vinalinux.findphone.base.activity.BaseActivity
import com.vinalinux.findphone.component.language.LanguageActivity
import com.vinalinux.findphone.component.main.MainActivity
import com.vinalinux.findphone.databinding.ActivitySplashBinding
import com.vinalinux.findphone.utils.SpManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.vinalinux.findphone.BuildConfig
import com.vinalinux.findphone.admob.BaseAdmob
import com.vinalinux.findphone.admob.InterAdmob
import com.vinalinux.findphone.admob.NameRemoteAdmob
import com.vinalinux.findphone.utils.NativeAdmobUtils

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
        if (spManager.getBoolean(NameRemoteAdmob.NATIVE_HOME, true)) {
            NativeAdmobUtils.loadNativeHome()
        }

        runProgress()
    }

    private fun runProgress() {
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