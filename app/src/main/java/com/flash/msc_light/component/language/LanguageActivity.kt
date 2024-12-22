package com.flash.msc_light.component.language

import android.app.Activity
import android.content.Intent
import androidx.activity.viewModels
import com.facebook.shimmer.ShimmerFrameLayout
import com.flash.msc_light.admob.BaseAdmob.OnAdmobShowListener
import com.flash.msc_light.admob.NativeAdmob
import com.flash.msc_light.base.activity.BaseActivity
import com.flash.msc_light.component.main.MainActivity
import com.flash.msc_light.component.onboarding.OnBoardingActivity
import com.flash.msc_light.databinding.ActivityLanguageCloneMscBinding
import com.flash.msc_light.domain.layer.LanguageModel
import com.flash.msc_light.utils.Constant
import com.flash.msc_light.utils.LocaleHelper
import com.flash.msc_light.utils.NativeAdmobUtils
import com.flash.msc_light.utils.SpManager
import com.flash.msc_light.utils.gone
import com.flash.msc_light.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LanguageActivity : BaseActivity<ActivityLanguageCloneMscBinding>() {
    private var selectLanguageModel: LanguageModel? = null
    private val viewModel: LanguageViewModel by viewModels()
    private val languageAdapter = LanguageAdapter()

    @Inject
    lateinit var spManager: SpManager

    override fun provideViewBinding(): ActivityLanguageCloneMscBinding {
        return ActivityLanguageCloneMscBinding.inflate(layoutInflater)
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
        viewBinding.flAdplaceholder2.root.gone()

        NativeAdmobUtils.languageNative2?.run {
            nativeAdLive?.observe(this@LanguageActivity){
                if(available()){
                    showNativeAd(this, viewBinding.flAdplaceholder2.root)
                }
            }
        }
    }

    override fun initObserver() {

        viewBinding.flAdplaceholder1.root.gone()

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
        if(spManager.getBoolean(SpManager.can_show_ads, true)){
            nativeAdmob.showNative(parent, object : OnAdmobShowListener{
                override fun onShow() {
                }

                override fun onError(e: String?) {
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