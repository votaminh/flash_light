package com.flash.msc_light.component.onboarding

import android.view.LayoutInflater
import android.view.ViewGroup
import com.flash.msc_light.base.adapter.BaseAdapter
import com.flash.msc_light.databinding.ItemOnboardingBinding

class OnBoardingAdapter : BaseAdapter<OnBoarding, ItemOnboardingBinding>() {

    override fun binData(viewBinding: ItemOnboardingBinding, item: OnBoarding) {
        viewBinding.apply {
//            tvIntro.setText(item.resDescription)
            tvTitle.setText(item.resDescription)
            imgOnBoarding.setImageResource(item.resImage)
        }
    }

    override fun provideViewBinding(parent: ViewGroup): ItemOnboardingBinding =
        ItemOnboardingBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
}