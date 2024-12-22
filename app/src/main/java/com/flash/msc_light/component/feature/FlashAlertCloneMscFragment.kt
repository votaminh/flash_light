package com.flash.msc_light.component.feature

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.flash.msc_light.BuildConfig
import com.flash.msc_light.R
import com.flash.msc_light.admob.BaseAdmob.OnAdmobLoadListener
import com.flash.msc_light.admob.BaseAdmob.OnAdmobShowListener
import com.flash.msc_light.admob.NativeAdmob
import com.flash.msc_light.base.fragment.BaseFragment
import com.flash.msc_light.component.PermissionActivity
import com.flash.msc_light.component.main.MainActivity
import com.flash.msc_light.databinding.FragmentFlashAlertCloneMscBinding
import com.flash.msc_light.utils.PermissionUtils
import com.flash.msc_light.utils.SpManager
import com.flash.msc_light.utils.gone
import com.flash.msc_light.utils.startNotificationFlashService
import com.flash.msc_light.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FlashAlertCloneMscFragment : BaseFragment<FragmentFlashAlertCloneMscBinding>() {

    val viewModel : FlashAlerCloneMscViewModel by viewModels()

    override fun provideViewBinding(container: ViewGroup?): FragmentFlashAlertCloneMscBinding {
        return FragmentFlashAlertCloneMscBinding.inflate(LayoutInflater.from(context))
    }


    private fun listenerView() {
        viewBinding.swTapHome.setOnClickListener {
            viewModel.saveStateFlash(viewBinding.swTapHome.isChecked)
        }
    }
    override fun initViews() {
        super.initViews()
        viewBinding.run {
            ftTurnOnComingCall.setOnClickListener {
                if(viewModel.stateLive.value == true){
                    checkPermissionToOpenSetting(SettingsFlashCloneMscAlertActivity.ALERT_CALL_PHONE)
                }
            }
            ftTurnOnNoti.setOnClickListener {
                if(viewModel.stateLive.value == true){
                    checkPermissionToOpenSetting(SettingsFlashCloneMscAlertActivity.ALERT_NOTIFICATION)
                }
            }
            ftTurnOnSms.setOnClickListener {
                if(viewModel.stateLive.value == true){
                    checkPermissionToOpenSetting(SettingsFlashCloneMscAlertActivity.ALERT_SMS)
                }
            }

            swFlashInSilent.setOnCheckedChangeListener { _, b ->
                viewModel.settingFlashInSilent(b)
            }
            swFlashInNormal.setOnCheckedChangeListener { _, b ->
                viewModel.settingFlashInNormalMode(b)
            }
            swFlashInVibrate.setOnCheckedChangeListener { _, b ->
                viewModel.settingFlashInVibrateMode(b)
            }
        }
        listenerView()
        activity?.startNotificationFlashService()

        showNative()
        viewBinding.flAdplaceholder.gone()
    }


    override fun initObserver() {
        viewModel.run {
            stateLive.observe(this@FlashAlertCloneMscFragment){
                viewBinding.swTapHome.isChecked = it
                if (it) {
                    viewBinding.layoutContainFeature.animate().alpha(1f).start()
                    viewBinding.tvStatus.text = getString(R.string.txt_tap_to_turn_off)
                } else {
                    viewBinding.layoutContainFeature.animate().alpha(0.3f).start()
                    viewBinding.tvStatus.text = getString(R.string.txt_tap_to_turn_on)
                }
            }

            flashWhenNormalModeLive.observe(this@FlashAlertCloneMscFragment){
                viewBinding.swFlashInNormal.isChecked = it
            }
            flashWhenVibrateModeLive.observe(this@FlashAlertCloneMscFragment){
                viewBinding.swFlashInVibrate.isChecked = it
            }
            flashWhenSilentModeLive.observe(this@FlashAlertCloneMscFragment){
                viewBinding.swFlashInSilent.isChecked = it
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
                            SettingsFlashCloneMscAlertActivity.start(it, type)
                        }
                    }
                }
            }else{
                PermissionActivity.start(it, PermissionActivity.KEY_FROM_MAIN)
            }
        }
    }
    private fun showNative() {
        context?.let { context ->
            if(SpManager.getInstance(context).getBoolean(SpManager.can_show_ads, true)){
                val nativeHome = NativeAdmob(context, BuildConfig.native_home)
                nativeHome.load(object : OnAdmobLoadListener{
                    override fun onLoad() {
                    }

                    override fun onError(e: String?) {
                    }
                })
                nativeHome.nativeAdLive.observe(this){
                    if(nativeHome.available()){
                        nativeHome.showNative(viewBinding.flAdplaceholder, object : OnAdmobShowListener{
                            override fun onShow() {
                            }

                            override fun onError(e: String?) {
                            }

                        })
                    }
                }
            }
        }
    }

    fun showNativeHome() {
        viewBinding.flAdplaceholder.visible()
    }
}