package com.flash.light.component.setting_alert

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.CompoundButton
import android.widget.SeekBar
import androidx.activity.viewModels
import com.flash.light.BuildConfig
import com.flash.light.R
import com.flash.light.admob.BaseAdmob.OnAdmobLoadListener
import com.flash.light.admob.BaseAdmob.OnAdmobShowListener
import com.flash.light.admob.NameRemoteAdmob
import com.flash.light.admob.NativeAdmob
import com.flash.light.base.activity.BaseActivity
import com.flash.light.databinding.ActivitySettingFlashAlertBinding
import com.flash.light.dialog.DialogExt.showDialogPermissionNotificationRead
import com.flash.light.utils.InterNativeUtils
import com.flash.light.utils.PermissionUtils
import com.flash.light.utils.SpManager
import com.flash.light.utils.startNotificationFlashService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFlashAlertActivity : BaseActivity<ActivitySettingFlashAlertBinding>() {

    private val viewModel : SettingFlashAlertViewModel by viewModels()

    private var type = ALERT_NORMAL

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
//        checkPermission("")
//

        viewBinding.run {
            btnTest.setOnClickListener {
                if(viewModel.isTestingLive.value == true){
                    viewModel.stopTest()
                }else{
                    viewModel.testFlash(type)
                }
            }

            head.imvBack.setOnClickListener {
                InterNativeUtils.showInterAction(this@SettingsFlashAlertActivity){
                    finish()
                }
            }
        }

        getData()
        setViewByType()
        viewModel.getValuesSetting(type)
        listenerView()
        showNative()
    }

    private fun showNative() {
        if(SpManager.getInstance(this).getBoolean(NameRemoteAdmob.native_function, true)){
            val nativeHome = NativeAdmob(this, BuildConfig.native_function)
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

    private fun listenerView() {
        viewBinding.run {
            swStatus.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
                override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                    viewModel.saveState(type, p1)
                }
            })

            swStatus.setOnClickListener {
                checkPermissionNotificationRead()
            }

            sbAlertOnTime.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    viewModel.saveOnTimePercent(type, p1)
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {

                }

                override fun onStopTrackingTouch(p0: SeekBar?) {

                }

            })
            sbAlertOffTime.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    viewModel.saveOffTimePercent(type, p1)
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {

                }

                override fun onStopTrackingTouch(p0: SeekBar?) {

                }

            })
        }
    }

    @SuppressLint("SetTextI18n")
    override fun initObserver() {
        super.initObserver()
        viewModel.run {
            stateLive.observe(this@SettingsFlashAlertActivity){
                viewBinding.swStatus.isChecked = it
                startNotificationFlashService()
            }

            onTimeLive.observe(this@SettingsFlashAlertActivity){
                viewBinding.secondAlertOnTime.text = it.toString() + " " + getString(R.string.txt_seconds)
            }
            offTimeLive.observe(this@SettingsFlashAlertActivity){
                viewBinding.secondAlertOffTime.text = it.toString() + " " + getString(R.string.txt_seconds)
            }
            progressSbOnTimeLive.observe(this@SettingsFlashAlertActivity){
                viewBinding.sbAlertOnTime.progress = it
            }
            progressSbOffTimeLive.observe(this@SettingsFlashAlertActivity){
                viewBinding.sbAlertOffTime.progress = it
            }
            isTestingLive.observe(this@SettingsFlashAlertActivity){
                if(it){
                    viewBinding.btnTest.text = getString(R.string.txt_stop)
                    viewBinding.disableSetting.visibility = View.VISIBLE
                }else{
                    viewBinding.btnTest.text = getString(R.string.txt_test)
                    viewBinding.disableSetting.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun getData() {
        type = intent.getStringExtra(KEY_TYPE_ALERT).toString()
    }

    private fun setViewByType() {
        viewBinding.run {
            when(type){
                ALERT_CALL_PHONE -> {
                    head.tvTitle.text = getString(R.string.txt_incoming_calls)
                    tvContentType.text = getString(R.string.txt_turn_on_for_incoming_calls)
                    ivType.setImageResource(R.drawable.ic_phone)

                }
                ALERT_NOTIFICATION -> {
                    head.tvTitle.text = getString(R.string.txt_notification)
                    tvContentType.text = getString(R.string.txt_turn_on_for_amp_notification)
                    ivType.setImageResource(R.drawable.ic_noti)
                }
                ALERT_SMS -> {
                    head.tvTitle.text = getString(R.string.txt_SMS)
                    tvContentType.text = getString(R.string.txt_turn_on_for_sms)
                    ivType.setImageResource(R.drawable.ic_sms)
                }
            }
        }
    }

    override fun provideViewBinding(): ActivitySettingFlashAlertBinding {
        return ActivitySettingFlashAlertBinding.inflate(layoutInflater)
    }


    private fun checkPermissionNotificationRead() {
        if(!PermissionUtils.isNotificationListenerPermission(this)){
            viewBinding.swStatus.isChecked = false
            showDialogPermissionNotificationRead{
                PermissionUtils.requestNotificationListenerPermission(this, 122)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 122){
            if(PermissionUtils.isNotificationListenerPermission(this)){
                viewBinding.swStatus.isChecked = true
            }
        }
    }

    override fun onDestroy() {
        viewModel.stopTest()
        super.onDestroy()
    }
}