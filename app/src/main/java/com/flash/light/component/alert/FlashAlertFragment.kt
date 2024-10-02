package com.flash.light.component.alert

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.viewModels
import com.facebook.ads.NativeAd
import com.flash.light.BuildConfig
import com.flash.light.R
import com.flash.light.admob.BaseAdmob
import com.flash.light.admob.NameRemoteAdmob
import com.flash.light.admob.NativeAdmob
import com.flash.light.base.fragment.BaseFragment
import com.flash.light.component.PermissionActivity
import com.flash.light.component.main.MainActivity
import com.flash.light.component.setting_alert.SettingsFlashAlertActivity
import com.flash.light.databinding.FragmentFlashAlertBinding
import com.flash.light.service.PhoneCallComingService
import com.flash.light.utils.PermissionUtils
import com.flash.light.utils.SpManager
import com.flash.light.utils.gone
import com.flash.light.utils.startNotificationFlashService
import com.flash.light.utils.visible
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

        showNative()
        viewBinding.flAdplaceholder.gone()
    }

    private fun showNative() {
        context?.let {
            if(SpManager.getInstance(it).getBoolean(NameRemoteAdmob.native_home, true)){
                val nativeHome = NativeAdmob(it, BuildConfig.native_home)
                nativeHome.load(null)
                nativeHome.nativeAdLive.observe(this){
                    if(nativeHome.available()){
                        nativeHome.showNative(viewBinding.flAdplaceholder, null)
                    }
                }
            }
        }
    }

    private fun listenerView() {
        viewBinding.swTapHome.setOnClickListener {
            viewModel.saveStateFlash(viewBinding.swTapHome.isChecked)
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
            if(PermissionUtils.isPhoneStatePermissionGranted(it) && PermissionUtils.isBatteryDisable(it)){
                activity?.let {
                    if(it is MainActivity){
                        it.showInterAction {
                            SettingsFlashAlertActivity.start(it, type)
                        }
                    }
                }
            }else{
                PermissionActivity.start(it, PermissionActivity.KEY_FROM_MAIN)
            }
        }
    }

    fun showNativeHome() {
        viewBinding.flAdplaceholder.visible()
    }
}