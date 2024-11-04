package com.flash.msc_light.component.feature

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.flash.msc_light.base.fragment.BaseFragment
import com.flash.msc_light.component.language.LanguageActivity
import com.flash.msc_light.databinding.FragmentSettingCloneMscBinding
import com.flash.msc_light.utils.AppUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingCloneMscFragment : BaseFragment<FragmentSettingCloneMscBinding>() {

    private val viewModel : SettingCloneMscViewModel by viewModels()

    override fun provideViewBinding(container: ViewGroup?): FragmentSettingCloneMscBinding {
        return FragmentSettingCloneMscBinding.inflate(LayoutInflater.from(context))
    }
    override fun initObserver() {
        viewModel.run {
            flashWhenScreenOnStateLive.observe(this@SettingCloneMscFragment){
                viewBinding.swNotFlashWhenScreenOn.isChecked = it
            }
            flashWhenBatteryLowLive.observe(this@SettingCloneMscFragment){
                viewBinding.swNotFlashWhenLowBattery.isChecked = it
            }
        }
    }
    override fun initViews() {
        viewBinding.run {
            swNotFlashWhenScreenOn.setOnCheckedChangeListener { _, b ->
                viewModel.settingFlashWhenScreenOn(b)
            }

            swNotFlashWhenLowBattery.setOnCheckedChangeListener { _, b ->
                viewModel.settingFlashWhenLowBattery(b)
            }

            policy.setOnClickListener {
                context?.let { it1 -> AppUtils.openLink(it1,"https://doc-hosting.flycricket.io/flash-alert-led-flashlight-privacy-policy/2ab51210-a0bb-4e77-8e6a-fac00effea73/privacy") }
            }

            share.setOnClickListener {
                context?.let { it1 -> AppUtils.shareApp(it1) }
            }

            language.setOnClickListener {
                activity?.let {
                    LanguageActivity.start(it, false)
                }
            }
        }

        viewModel.checkSettingValues()
    }


}