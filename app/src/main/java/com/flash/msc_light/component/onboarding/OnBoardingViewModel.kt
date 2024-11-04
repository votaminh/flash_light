package com.flash.msc_light.component.onboarding

import com.flash.msc_light.R
import com.flash.msc_light.base.viewmodel.BaseViewModel

class OnBoardingViewModel : BaseViewModel() {
    val listOnBoarding = listOf(
        OnBoarding(
            R.drawable.ic_onboarding1_clone_msc,
            R.string.onboarding_title1,
            R.string.onboarding_intro1
        ),
        OnBoarding(
            R.drawable.ic_onboarding2_clone_msc,
            R.string.onboarding_title1,
            R.string.onboarding_intro2
        ),
        OnBoarding(
            R.drawable.ic_onboarding3_clone_msc,
            R.string.onboarding_title1,
            R.string.onboarding_intro3
        )
    )
}