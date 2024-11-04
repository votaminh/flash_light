package com.flash.light.component

import android.app.Activity
import android.content.Intent
import com.flash.light.admob.BaseAdmob.OnAdmobShowListener
import com.flash.light.base.activity.BaseActivity
import com.flash.light.component.main.MainActivity
import com.flash.light.databinding.ActivityPermisionBinding
import com.flash.light.utils.NativeAdmobUtils
import com.flash.light.utils.PermissionUtils

class PermissionActivity : BaseActivity<ActivityPermisionBinding>() {

    companion object {
        const val KEY_FROM_ONBOARDING = 1
        const val KEY_FROM_MAIN = 2
        const val KEY_NAME_FROM = "KEY_NAME_FROM"

        fun start(activity : Activity, from : Int = KEY_FROM_ONBOARDING){
            val intent = Intent(activity, PermissionActivity::class.java)
            intent.putExtra(KEY_NAME_FROM, from)
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
                actionNext()
            }



        }

        if(NativeAdmobUtils.permissionNative == null){
            NativeAdmobUtils.loadNativePermission()
        }

        NativeAdmobUtils.permissionNative?.run {
            nativeAdLive.observe(this@PermissionActivity){
                if(available()){
                    showNative(viewBinding.flAdplaceholder, object : OnAdmobShowListener{
                        override fun onShow() {
                        }

                        override fun onError(e: String?) {
                        }

                    })
                }
            }
        }

        val grant = PermissionUtils.isPhoneStatePermissionGranted(this@PermissionActivity)
        val grantBattery = PermissionUtils.isBatteryDisable(this@PermissionActivity)

        if(grant && grantBattery){
            actionNext()
        }
    }

    private fun actionNext() {
        val from = intent.getIntExtra(KEY_NAME_FROM, KEY_FROM_ONBOARDING)
        if(from == KEY_FROM_ONBOARDING){
            MainActivity.start(this@PermissionActivity)
        }
        finish()
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