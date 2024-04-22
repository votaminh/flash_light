package com.flash.light.component.language

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.nativead.NativeAd
import com.flash.light.R
import com.flash.light.admob.NameRemoteAdmob
import com.flash.light.base.activity.BaseActivity
import com.flash.light.component.main.MainActivity
import com.flash.light.component.onboarding.OnBoardingActivity
import com.flash.light.databinding.ActivityLanguageBinding
import com.flash.light.utils.NativeAdmobUtils
import com.flash.light.utils.NetworkUtil
import com.flash.light.utils.SpManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LanguageActivity : BaseActivity<ActivityLanguageBinding>() {
    private val viewModel: LanguageViewModel by viewModels()
    private val languageAdapter = LanguageAdapter()

    @Inject
    lateinit var spManager: SpManager

    override fun provideViewBinding(): ActivityLanguageBinding {
        return ActivityLanguageBinding.inflate(layoutInflater)
    }

    override fun initViews() {

        setStatusBarColor(ContextCompat.getColor(this, R.color.white), true)


        viewBinding.rclLanguage.adapter = languageAdapter
        languageAdapter.onClick = {
            languageAdapter.selectLanguage(it.languageCode)
        }
        viewBinding.ivDone.setOnClickListener {
            languageAdapter.selectedLanguage()?.let { languageModel ->
                spManager.saveLanguage(languageModel)
//                setAppLanguage(languageModel.languageCode)
                val fromSplash = intent.getBooleanExtra(KEY_FROM_SPLASH, false)
                if (fromSplash) {
                    OnBoardingActivity.start(this)
                    finish()
                } else {
                    startActivity(Intent(this, MainActivity::class.java).also {
                        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    })
                }
            }
        }

        if(spManager.getShowOnBoarding()){
            viewBinding.ivDone.setText(R.string.txt_next)
        }

        if (isTempNativeAd != null) {
            isTempNativeAd = null
        }

        if (spManager.getShowOnBoarding() && NetworkUtil.isOnline) {
            if (spManager.getBoolean(NameRemoteAdmob.NATIVE_ONBOARD, true)) {
                NativeAdmobUtils.loadNativeOnboard()
            }
        }
    }

    override fun initObserver() {
        viewModel.listLanguage.observe(this) {
            languageAdapter.setData(ArrayList(it))
            languageAdapter.selectLanguage(spManager.getLanguage().languageCode)
        }

        viewBinding.flAdplaceholder.visibility = View.GONE
        NativeAdmobUtils.languageNativeAdmob?.run {
            nativeAdLive?.observe(this@LanguageActivity){
                if(available() && spManager.getBoolean(NameRemoteAdmob.NATIVE_LANGUAGE, true)){
                    viewBinding.flAdplaceholder.visibility = View.VISIBLE
                    showNative(viewBinding.flAdplaceholder, null)
                }
            }
        }
    }

    override fun initData() {
        viewModel.loadListLanguage()
    }

    override fun onDestroy() {
        isTempNativeAd = null
        super.onDestroy()
    }

    companion object {
        var isTempNativeAd: NativeAd? = null
        const val KEY_FROM_SPLASH = "key_splash"
        fun start(context: Context, fromSplash: Boolean = false) {
            Intent(context, LanguageActivity::class.java).also {
                it.putExtra(KEY_FROM_SPLASH, fromSplash)
                context.startActivity(it)
            }
        }
    }
}