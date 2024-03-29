package com.flash.light.component.onboarding

import com.flash.light.R
import com.flash.light.base.viewmodel.BaseViewModel

class OnBoardingViewModel : BaseViewModel() {
    val listOnBoarding = listOf(
        OnBoarding(
            R.drawable.ic_onboarding1,
            R.string.onboarding_title1,
            R.string.onboarding_intro1
        ),
        OnBoarding(
            R.drawable.ic_onboarding2,
            R.string.onboarding_title1,
            R.string.onboarding_intro2
        ),
        OnBoarding(
            R.drawable.ic_onboarding3,
            R.string.onboarding_title1,
            R.string.onboarding_intro3
        )
    )
}