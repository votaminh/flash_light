package com.flash.light.component.setting

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.flash.light.base.fragment.BaseFragment
import com.flash.light.databinding.FragmentSettingBinding
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