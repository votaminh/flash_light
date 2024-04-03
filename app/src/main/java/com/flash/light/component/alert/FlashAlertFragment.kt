package com.flash.light.component.alert

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.viewModels
import com.flash.light.R
import com.flash.light.base.fragment.BaseFragment
import com.flash.light.component.PermissionActivity
import com.flash.light.component.setting_alert.SettingsFlashAlertActivity
import com.flash.light.databinding.FragmentFlashAlertBinding
import com.flash.light.service.PhoneCallComingService
import com.flash.light.utils.PermissionUtils
import com.flash.light.utils.SpManager
import com.flash.light.utils.startNotificationFlashService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FlashAlertFragment : BaseFragment<FragmentFlashAlertBinding>() {

    val viewModel : FlashAlertViewModel by viewModels()

    override fun provideViewBinding(container: ViewGroup?): FragmentFlashAlertBinding {
        return FragmentFlashAlertBinding.inflate(LayoutInflater.from(context))
    }

    override fun initViews() {
        super.initViews()
        viewBinding.run {
            ftTurnOnComingCall.setOnClickListener {
                if(viewModel.stateLive.value == true){
                    checkPermissionToOpenSetting(SettingsFlashAlertActivity.ALERT_CALL_PHONE)
                }
            }
            ftTurnOnNoti.setOnClickListener {
                if(viewModel.stateLive.value == true){
                    checkPermissionToOpenSetting(SettingsFlashAlertActivity.ALERT_NOTIFICATION)
                }
            }
            ftTurnOnSms.setOnClickListener {
                if(viewModel.stateLive.value == true){
                    checkPermissionToOpenSetting(SettingsFlashAlertActivity.ALERT_SMS)
                }
            }
        }
        listenerView()
        activity?.startNotificationFlashService()
    }

    private fun listenerView() {
        viewBinding.swTapHome.setOnCheckedChangeListener { p0, p1 ->
            viewModel.saveStateFlash(p1)
        }
    }

    override fun initObserver() {
        viewModel.run {
            stateLive.observe(this@FlashAlertFragment){
                viewBinding.swTapHome.isChecked = it
                if (it) {
                    viewBinding.layoutContainFeature.animate().alpha(1f).start()
                    viewBinding.tvStatus.text = getString(R.string.txt_tap_to_turn_off)
                } else {
                    viewBinding.layoutContainFeature.animate().alpha(0.3f).start()
                    viewBinding.tvStatus.text = getString(R.string.txt_tap_to_turn_on)
                }
            }
        }

        viewModel.checkState()
    }

    private fun checkPermissionToOpenSetting(type: String) {
        activity?.let {
            if(PermissionUtils.isPhoneStatePermissionGranted(it)){
                SettingsFlashAlertActivity.start(it, type)
            }else{
                PermissionActivity.start(it)
            }
        }
    }
}