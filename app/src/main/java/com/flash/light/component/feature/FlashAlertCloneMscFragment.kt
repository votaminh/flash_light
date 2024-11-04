package com.flash.light.component.feature

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.flash.light.BuildConfig
import com.flash.light.R
import com.flash.light.admob.BaseAdmob.OnAdmobLoadListener
import com.flash.light.admob.BaseAdmob.OnAdmobShowListener
import com.flash.light.admob.NameRemoteAdmob
import com.flash.light.admob.NativeAdmob
import com.flash.light.base.fragment.BaseFragment
import com.flash.light.component.PermissionActivity
import com.flash.light.component.main.MainActivity
import com.flash.light.databinding.FragmentFlashAlertBinding
import com.flash.light.utils.PermissionUtils
import com.flash.light.utils.SpManager
import com.flash.light.utils.gone
import com.flash.light.utils.startNotificationFlashService
import com.flash.light.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FlashAlertCloneMscFragment : BaseFragment<FragmentFlashAlertBinding>() {

    val viewModel : FlashAlerCloneMscViewModel by viewModels()

    override fun provideViewBinding(container: ViewGroup?): FragmentFlashAlertBinding {
        return FragmentFlashAlertBinding.inflate(LayoutInflater.from(context))
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
        }
        listenerView()
        activity?.startNotificationFlashService()

        showNative()
        viewBinding.flAdplaceholder.gone()
    }

    private fun showNative() {
        context?.let { context ->
            if(SpManager.getInstance(context).getBoolean(NameRemoteAdmob.native_home, true)){
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

    private fun listenerView() {
        viewBinding.swTapHome.setOnClickListener {
            viewModel.saveStateFlash(viewBinding.swTapHome.isChecked)
        }
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

    fun showNativeHome() {
        viewBinding.flAdplaceholder.visible()
    }
}