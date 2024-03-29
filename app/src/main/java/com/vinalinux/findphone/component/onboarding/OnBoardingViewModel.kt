package com.vinalinux.findphone.component.onboarding

import com.vinalinux.findphone.R
import com.vinalinux.findphone.base.viewmodel.BaseViewModel

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