package com.vinalinux.findphone.component.language

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.activity.viewModels
import com.google.android.gms.ads.nativead.NativeAd
import com.vinalinux.findphone.R
import com.vinalinux.findphone.admob.NameRemoteAdmob
import com.vinalinux.findphone.base.activity.BaseActivity
import com.vinalinux.findphone.component.main.MainActivity
import com.vinalinux.findphone.component.onboarding.OnBoardingActivity
import com.vinalinux.findphone.databinding.ActivityLanguageBinding
import com.vinalinux.findphone.utils.NativeAdmobUtils
import com.vinalinux.findphone.utils.SpManager
import com.vinalinux.findphone.utils.setAppLanguage
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
        viewBinding.rclLanguage.adapter = languageAdapter
        languageAdapter.onClick = {
            languageAdapter.selectLanguage(it.languageCode)
        }
        viewBinding.ivDone.setOnClickListener {
            languageAdapter.selectedLanguage()?.let { languageModel ->
                spManager.saveLanguage(languageModel)
                setAppLanguage(languageModel.languageCode)
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
        viewBinding.ivBack.setOnClickListener {
            finish()
        }

        if(spManager.getShowOnBoarding()){
            viewBinding.ivDone.setText(R.string.txt_next)
        }else {
            viewBinding.ivDone.setText(R.string.txt_save)
        }

        if (isTempNativeAd != null) {
            isTempNativeAd = null
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