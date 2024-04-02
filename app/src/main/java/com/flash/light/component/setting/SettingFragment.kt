package com.flash.light.component.setting

import android.view.LayoutInflater
import android.view.ViewGroup
import com.flash.light.base.fragment.BaseFragment
import com.flash.light.databinding.FragmentSettingBinding

class SettingFragment : BaseFragment<FragmentSettingBinding>() {
    override fun provideViewBinding(container: ViewGroup?): FragmentSettingBinding {
        return FragmentSettingBinding.inflate(LayoutInflater.from(context))
    }
}