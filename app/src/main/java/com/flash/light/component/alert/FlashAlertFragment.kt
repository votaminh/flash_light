package com.flash.light.component.alert

import android.view.LayoutInflater
import android.view.ViewGroup
import com.flash.light.base.fragment.BaseFragment
import com.flash.light.component.setting_alert.SettingsFlashAlertActivity
import com.flash.light.databinding.FragmentFlashAlertBinding

class FlashAlertFragment : BaseFragment<FragmentFlashAlertBinding>() {
    override fun provideViewBinding(container: ViewGroup?): FragmentFlashAlertBinding {
        return FragmentFlashAlertBinding.inflate(LayoutInflater.from(context))
    }

    override fun initViews() {
        super.initViews()
        viewBinding.run {
            ftTurnOnComingCall.setOnClickListener {
                activity?.let {
                    SettingsFlashAlertActivity.start(it, SettingsFlashAlertActivity.ALERT_CALL_PHONE)
                }
            }
        }
    }
}