package com.flash.light.component.onboarding

import android.content.Context
import android.content.Intent
import android.os.Looper
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.flash.light.admob.NameRemoteAdmob
import com.flash.light.base.activity.BaseActivity
import com.flash.light.component.PermissionActivity
import com.flash.light.databinding.ActivityOnboardingBinding
import com.flash.light.utils.NativeAdmobUtils
import com.flash.light.utils.SpManager
import com.flash.light.utils.gone
import com.flash.light.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingActivity : BaseActivity<ActivityOnboardingBinding>() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, OnBoardingActivity::class.java))
        }
    }

    override fun provideViewBinding(): ActivityOnboardingBinding =
        ActivityOnboardingBinding.inflate(layoutInflater)

    private val viewModel: OnBoardingViewModel by viewModels()

    private val onBoardingAdapter = OnBoardingAdapter()
    private var currentPosition = 0

    override fun initViews() {
        viewBinding.apply {
            vpOnBoarding.adapter = onBoardingAdapter

            vpOnBoarding.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    currentPosition = position

                    showNative(currentPosition)
                }
            })

            buttonNext.setOnClickListener {
                if (currentPosition < viewModel.listOnBoarding.size - 1) {
                    vpOnBoarding.setCurrentItem(currentPosition + 1, true)
                } else {
                    PermissionActivity.start(this@OnBoardingActivity)
                    finish()
                }
            }

            dotIndicator.attachTo(vpOnBoarding)
            onBoardingAdapter.setData(ArrayList(viewModel.listOnBoarding))
        }

        if(SpManager.getInstance(this).getBoolean(NameRemoteAdmob.native_onboarding, true)){
            NativeAdmobUtils.loadNativePermission()
        }
    }

    private fun showNative(currentPosition: Int) {

        if(!SpManager.getInstance(this).getBoolean(NameRemoteAdmob.native_onboarding, true)){
            viewBinding.flAdplaceholder.gone()
            return
        }

        if(currentPosition == 1){
            viewBinding.flAdplaceholder.gone()
        }else{
            viewBinding.flAdplaceholder.visible()
        }

        when(currentPosition){
            0 -> {
                NativeAdmobUtils.onboardNativeAdmob1?.run {
                    nativeAdLive?.observe(this@OnBoardingActivity){
                        if(available()){
                            android.os.Handler(Looper.getMainLooper()).postDelayed({
                                showNative(viewBinding.flAdplaceholder, null)
                            }, 100)
                        }
                    }
                }
            }

            2 -> {
                NativeAdmobUtils.onboardNativeAdmob2?.run {
                    nativeAdLive?.observe(this@OnBoardingActivity){
                        if(available()){
                            android.os.Handler(Looper.getMainLooper()).postDelayed({
                                showNative(viewBinding.flAdplaceholder, null)
                            }, 100)
                        }
                    }
                }
            }
        }
    }
}