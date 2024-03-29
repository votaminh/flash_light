package com.vinalinux.findphone.component.onboarding

import android.view.LayoutInflater
import android.view.ViewGroup
import com.vinalinux.findphone.base.adapter.BaseAdapter
import com.vinalinux.findphone.databinding.ItemOnboardingBinding

class OnBoardingAdapter : BaseAdapter<OnBoarding, ItemOnboardingBinding>() {

    override fun binData(viewBinding: ItemOnboardingBinding, item: OnBoarding) {
        viewBinding.apply {
            tvIntro.setText(item.resDescription)
            tvTitle.setText(item.resTitle)
            imgOnBoarding.setImageResource(item.resImage)
        }
    }

    override fun provideViewBinding(parent: ViewGroup): ItemOnboardingBinding =
        ItemOnboardingBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
}