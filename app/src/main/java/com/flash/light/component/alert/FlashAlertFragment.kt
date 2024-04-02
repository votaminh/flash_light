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

class FlashAlertFragment : BaseFragment<FragmentFlashAlertBinding>() {
    override fun provideViewBinding(container: ViewGroup?): FragmentFlashAlertBinding {
        return FragmentFlashAlertBinding.inflate(LayoutInflater.from(context))
    }

    override fun initViews() {
        super.initViews()
        viewBinding.run {
            ftTurnOnComingCall.setOnClickListener {
                activity?.let {
                    if(PermissionUtils.isPhoneStatePermissionGranted(it)){
                        SettingsFlashAlertActivity.start(it, SettingsFlashAlertActivity.ALERT_CALL_PHONE)
                    }else{
                        PermissionActivity.start(it)
                    }
                }
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