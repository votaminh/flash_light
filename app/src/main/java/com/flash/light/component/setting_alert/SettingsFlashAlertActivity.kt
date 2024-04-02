package com.flash.light.component.setting_alert

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.flash.light.base.activity.BaseActivity
import com.flash.light.databinding.ActivitySettingFlashAlertBinding
import com.flash.light.utils.PermissionUtils

class SettingsFlashAlertActivity : BaseActivity<ActivitySettingFlashAlertBinding>() {

    companion object {
        private const val KEY_TYPE_ALERT = "KEY_TYPE_ALERT"
        const val ALERT_CALL_PHONE = "ALERT_CALL_PHONE"
        const val ALERT_NORMAL = "ALERT_NORMAL"
        const val ALERT_NOTIFICATION = "ALERT_NOTIFICATION"
        const val ALERT_SMS = "ALERT_SMS"

        fun start(activity : Activity, type : String){
            val intent = Intent(activity, SettingsFlashAlertActivity::class.java)
            intent.putExtra(KEY_TYPE_ALERT, type)
            activity.startActivity(intent)
        }
    }

    override fun initViews() {
        super.initViews()
        checkPermission("")
    }

    override fun provideViewBinding(): ActivitySettingFlashAlertBinding {
        return ActivitySettingFlashAlertBinding.inflate(layoutInflater)
    }

    private fun checkPermission(permissionType: String) {
        if(!PermissionUtils.isPhoneStatePermissionGranted(this)){
            PermissionUtils.requestPhoneStatePermission(this, 123)
        }
    }
}