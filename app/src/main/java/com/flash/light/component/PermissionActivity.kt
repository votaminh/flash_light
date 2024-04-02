package com.flash.light.component

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.flash.light.base.activity.BaseActivity
import com.flash.light.component.setting_alert.SettingsFlashAlertActivity
import com.flash.light.databinding.ActivityPermisionBinding
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

            layoutContinue.setOnClickListener {
                finish()
            }
        }
    }

    private fun updateSwManagerCall() {
        viewBinding.run {
            val grant = PermissionUtils.isPhoneStatePermissionGranted(this@PermissionActivity)
            swPermissionManagerCall.isChecked = grant
            if(grant){
                layoutContinue.animate().alpha(1f).setDuration(0).start()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 312){
            updateSwManagerCall()
        }
    }
}