package com.flash.light.component

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.flash.light.base.activity.BaseActivity
import com.flash.light.component.setting_alert.SettingsFlashAlertActivity
import com.flash.light.databinding.ActivityPermisionBinding
import com.flash.light.utils.NativeAdmobUtils
import com.flash.light.utils.PermissionUtils

class PermissionActivity : BaseActivity<ActivityPermisionBinding>() {

    companion object {
        fun start(activity : Activity){
            val intent = Intent(activity, PermissionActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun provideViewBinding(): ActivityPermisionBinding {
        return ActivityPermisionBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        super.initViews()
        viewBinding.run {
            updateSwManagerCall()
            swPermissionManagerCall.setOnClickListener {
                if(!PermissionUtils.isPhoneStatePermissionGranted(this@PermissionActivity)){
                    PermissionUtils.requestPhoneStatePermission(this@PermissionActivity,312)
                }
            }

            swPermissionCamera.setOnClickListener {
                if(!PermissionUtils.isBatteryDisable(this@PermissionActivity)){
                    PermissionUtils.requestBatteryDisable(this@PermissionActivity,312)
                }
            }

            layoutContinue.setOnClickListener {
                finish()
            }
        }

        NativeAdmobUtils.permissionNative?.run {
            nativeAdLive.observe(this@PermissionActivity){
                if(available()){
                    showNative(viewBinding.flAdplaceholder, null)
                }
            }
        }
    }

    private fun updateSwManagerCall() {
        viewBinding.run {
            val grant = PermissionUtils.isPhoneStatePermissionGranted(this@PermissionActivity)
            swPermissionManagerCall.isChecked = grant

            val grantBattery = PermissionUtils.isBatteryDisable(this@PermissionActivity)
            swPermissionCamera.isChecked = grantBattery
        }

        checkPermissionAllow()
    }

    private fun checkPermissionAllow() {
        val grant = PermissionUtils.isPhoneStatePermissionGranted(this@PermissionActivity)
        val grantBattery = PermissionUtils.isBatteryDisable(this@PermissionActivity)

        if(grant && grantBattery){
            viewBinding.layoutContinue.animate().alpha(1f).start()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        updateSwManagerCall()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        updateSwManagerCall()
    }
}