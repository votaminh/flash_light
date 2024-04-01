package com.flash.light.component.light

import android.view.LayoutInflater
import android.view.ViewGroup
import com.flash.light.base.fragment.BaseFragment
import com.flash.light.databinding.FragmentFlashLightBinding

class FlashLightFragment : BaseFragment<FragmentFlashLightBinding>() {
    override fun provideViewBinding(container: ViewGroup?): FragmentFlashLightBinding {
        return FragmentFlashLightBinding.inflate(LayoutInflater.from(context))
    }
}