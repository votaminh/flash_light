package com.flash.light.component.setting

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.flash.light.base.fragment.BaseFragment
import com.flash.light.component.language.LanguageActivity
import com.flash.light.databinding.FragmentSettingBinding
import com.flash.light.utils.AppUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding>() {

    private val viewModel : SettingViewModel by viewModels()

    override fun provideViewBinding(container: ViewGroup?): FragmentSettingBinding {
        return FragmentSettingBinding.inflate(LayoutInflater.from(context))
    }

    override fun initViews() {
        viewBinding.run {
            swNotFlashWhenScreenOn.setOnCheckedChangeListener { _, b ->
                viewModel.settingFlashWhenScreenOn(b)
            }
            swNotFlashWhenLowBattery.setOnCheckedChangeListener { _, b ->
                viewModel.settingFlashWhenLowBattery(b)
            }
            swFlashInNormal.setOnCheckedChangeListener { _, b ->
                viewModel.settingFlashInNormalMode(b)
            }
            swFlashInVibrate.setOnCheckedChangeListener { _, b ->
                viewModel.settingFlashInVibrateMode(b)
            }
            swFlashInSilent.setOnCheckedChangeListener { _, b ->
                viewModel.settingFlashInSilent(b)
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

    override fun initObserver() {
        viewModel.run {
            flashWhenScreenOnStateLive.observe(this@SettingFragment){
                viewBinding.swNotFlashWhenScreenOn.isChecked = it
            }
            flashWhenBatteryLowLive.observe(this@SettingFragment){
                viewBinding.swNotFlashWhenLowBattery.isChecked = it
            }
            flashWhenNormalModeLive.observe(this@SettingFragment){
                viewBinding.swFlashInNormal.isChecked = it
            }
            flashWhenVibrateModeLive.observe(this@SettingFragment){
                viewBinding.swFlashInVibrate.isChecked = it
            }
            flashWhenSilentModeLive.observe(this@SettingFragment){
                viewBinding.swFlashInSilent.isChecked = it
            }
        }
    }
}