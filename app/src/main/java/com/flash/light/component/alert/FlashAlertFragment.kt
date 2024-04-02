package com.flash.light.component.alert

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.flash.light.base.fragment.BaseFragment
import com.flash.light.component.PermissionActivity
import com.flash.light.component.setting_alert.SettingsFlashAlertActivity
import com.flash.light.databinding.FragmentFlashAlertBinding
import com.flash.light.service.PhoneCallComingService
import com.flash.light.utils.PermissionUtils
import com.flash.light.utils.SpManager
import javax.inject.Inject

class FlashAlertFragment : BaseFragment<FragmentFlashAlertBinding>() {

    @Inject
    lateinit var spManager: SpManager

    override fun provideViewBinding(container: ViewGroup?): FragmentFlashAlertBinding {
        return FragmentFlashAlertBinding.inflate(LayoutInflater.from(context))
    }

    override fun initViews() {
        super.initViews()
        viewBinding.run {
            ftTurnOnComingCall.setOnClickListener {
                checkPermissionToOpenSetting(SettingsFlashAlertActivity.ALERT_CALL_PHONE)
            }
            ftTurnOnNoti.setOnClickListener {
                checkPermissionToOpenSetting(SettingsFlashAlertActivity.ALERT_NOTIFICATION)
            }
            ftTurnOnSms.setOnClickListener {
                checkPermissionToOpenSetting(SettingsFlashAlertActivity.ALERT_SMS)
            }
        }
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

    private fun playCallPhoneService() {
        activity?.let {
            it.startService(Intent(it, PhoneCallComingService::class.java))
        }
    }

    private fun stopCallPhoneService() {
        activity?.let {
            it.stopService(Intent(it, PhoneCallComingService::class.java))
        }
    }

    private fun isStartedService(): Boolean {
//        val systemService = getSystemService(android.content.Context.ACTIVITY_SERVICE)
//        Intrinsics.checkNotNull(
//            systemService,
//            "null cannot be cast to non-null type android.app.ActivityManager"
//        )
//        for (runningServiceInfo in (systemService as ActivityManager).getRunningServices(
//            Int.MAX_VALUE
//        )) {
//            if (Intrinsics.areEqual(
//                    runningServiceInfo.service.className as Any,
//                    PhoneCallComingService::class.java.getName() as Any
//                )
//            ) {
//                return true
//            }
//        }
        return false
    }
}