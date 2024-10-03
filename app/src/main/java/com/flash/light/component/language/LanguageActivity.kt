package com.flash.light.component.language

import android.app.Activity
import android.content.Intent
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.facebook.appevents.AppEventsLogger
import com.facebook.shimmer.ShimmerFrameLayout
import com.flash.light.admob.BaseAdmob.OnAdmobShowListener
import com.flash.light.admob.NameRemoteAdmob
import com.flash.light.admob.NativeAdmob
import com.flash.light.base.activity.BaseActivity
import com.flash.light.component.main.MainActivity
import com.flash.light.component.onboarding.OnBoardingActivity
import com.flash.light.databinding.ActivityLanguageBinding
import com.flash.light.domain.layer.LanguageModel
import com.flash.light.utils.Constant
import com.flash.light.utils.LocaleHelper
import com.flash.light.utils.NativeAdmobUtils
import com.flash.light.utils.SpManager
import com.flash.light.utils.gone
import com.flash.light.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LanguageActivity : BaseActivity<ActivityLanguageBinding>() {
    private var selectLanguageModel: LanguageModel? = null
    private val viewModel: LanguageViewModel by viewModels()
    private val languageAdapter = LanguageAdapter()

    @Inject
    lateinit var spManager: SpManager

    override fun provideViewBinding(): ActivityLanguageBinding {
        return ActivityLanguageBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        val isFromSplash = intent.getBooleanExtra(Constant.KEY_INTENT_FROM_SPLASH, false)

        viewBinding.ivDone.gone()
        viewBinding.rclLanguage.adapter = languageAdapter
        languageAdapter.onClick = {languageModel ->
            viewBinding.ivDone.visible()
            selectLanguageModel = languageModel
            languageAdapter.selectLanguage(languageModel.languageCode)
            showNative2()
        }
        viewBinding.ivDone.setOnClickListener {
            if (selectLanguageModel == null) {
                selectLanguageModel = languageAdapter.dataSet.find { it.selected }
            }
            selectLanguageModel?.let { it1 -> spManager.saveLanguage(it1) }
            LocaleHelper.setLocale(this@LanguageActivity, language = selectLanguageModel?.languageCode ?: "en")
            if (isFromSplash) {
                OnBoardingActivity.start(this)
                finish()
            } else {
                MainActivity.start(this@LanguageActivity)
                finish()
            }
        }

        NativeAdmobUtils.loadNativeOnboard()
    }

    private fun showNative2() {
        viewBinding.flAdplaceholder1.root.gone()
        viewBinding.flAdplaceholder2.root.visible()

        NativeAdmobUtils.languageNative2?.run {
            nativeAdLive?.observe(this@LanguageActivity){
                if(available()){
                    showNativeAd(this, viewBinding.flAdplaceholder2.root)
                }
            }
        }
    }

    override fun initObserver() {
        viewModel.listLanguage.observe(this) {
            languageAdapter.setData(ArrayList(it))
//            languageAdapter.selectLanguage(spManager.getLanguage().languageCode)
        }

        NativeAdmobUtils.languageNative1?.run {
            nativeAdLive?.observe(this@LanguageActivity){
                if(available()){
                    showNativeAd(this, viewBinding.flAdplaceholder1.root)
                }
            }
        }
    }

    private fun showNativeAd(nativeAdmob: NativeAdmob, parent : ShimmerFrameLayout) {
        AppEventsLogger.newLogger(this@LanguageActivity).logEvent("native_language_show")
        if(spManager.getBoolean(NameRemoteAdmob.native_language, true)){
            nativeAdmob.showNative(parent, object : OnAdmobShowListener{
                override fun onShow() {
                    AppEventsLogger.newLogger(this@LanguageActivity).logEvent("native_language_show_success")
                }

                override fun onError(e: String?) {
                    AppEventsLogger.newLogger(this@LanguageActivity).logEvent("native_language_show_fail")
                }

            })
            parent.visible()
        }else{
            parent.gone()
        }
    }

    override fun initData() {
        viewModel.loadListLanguage()
    }

    companion object {
        fun start(activity: Activity, fromSplash : Boolean) {
            val intent = Intent(activity, LanguageActivity::class.java)
            intent.putExtra(Constant.KEY_INTENT_FROM_SPLASH, fromSplash)
            activity.startActivity(intent)
        }
    }
}