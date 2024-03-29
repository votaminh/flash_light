package com.vinalinux.findphone.component.onboarding

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.vinalinux.findphone.BuildConfig
import com.vinalinux.findphone.R
import com.vinalinux.findphone.admob.NameRemoteAdmob
import com.vinalinux.findphone.base.activity.BaseActivity
import com.vinalinux.findphone.component.permission.PermissionActivity
import com.vinalinux.findphone.databinding.ActivityOnboardingBinding
import com.vinalinux.findphone.utils.NativeAdmobUtils
import com.vinalinux.findphone.utils.RemoteConfig
import com.vinalinux.findphone.utils.SpManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OnBoardingActivity : BaseActivity<ActivityOnboardingBinding>() {

    @Inject
    lateinit var spManager: SpManager

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

    override var isReloadInter: Boolean = false

    override fun initViews() {
        viewBinding.apply {
            vpOnBoarding.adapter = onBoardingAdapter

            vpOnBoarding.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    currentPosition = position

                    if(currentPosition == 2){
                        viewBinding.buttonNext.setText(R.string.txt_get_start)
                    }else{
                        viewBinding.buttonNext.setText(R.string.txt_next)
                    }
                }
            })

            buttonNext.setOnClickListener {
                if (currentPosition < viewModel.listOnBoarding.size - 1) {
                    vpOnBoarding.setCurrentItem(currentPosition + 1, true)
                } else {
                    startActivity(PermissionActivity.getIntent(this@OnBoardingActivity))
                    finish()
                }
            }

            tvSkip.setOnClickListener {
                startActivity(PermissionActivity.getIntent(this@OnBoardingActivity))
                finish()
            }

            dotIndicator.attachTo(vpOnBoarding)
            onBoardingAdapter.setData(ArrayList(viewModel.listOnBoarding))
        }
    }

    override fun initObserver() {
        super.initObserver()
        viewBinding.flAdplaceholder.visibility = View.GONE
        NativeAdmobUtils.onboardNativeAdmob?.run {
            nativeAdLive?.observe(this@OnBoardingActivity){
                if(available() && spManager.getBoolean(NameRemoteAdmob.NATIVE_ONBOARD, true)){
                    viewBinding.flAdplaceholder.visibility = View.VISIBLE
                    showNative(viewBinding.flAdplaceholder, null)
                }
            }
        }
    }
}