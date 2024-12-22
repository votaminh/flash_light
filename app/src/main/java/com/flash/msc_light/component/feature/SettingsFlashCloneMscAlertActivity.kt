package com.flash.msc_light.component.feature

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.CompoundButton
import android.widget.SeekBar
import androidx.activity.viewModels
import com.flash.msc_light.BuildConfig
import com.flash.msc_light.R
import com.flash.msc_light.admob.BaseAdmob.OnAdmobLoadListener
import com.flash.msc_light.admob.BaseAdmob.OnAdmobShowListener
import com.flash.msc_light.admob.NativeAdmob
import com.flash.msc_light.base.activity.BaseActivity
import com.flash.msc_light.databinding.ActivitySettingFlashAlertBinding
import com.flash.msc_light.dialog.DialogExt.showDialogPermissionNotificationRead
import com.flash.msc_light.utils.InterNativeUtils
import com.flash.msc_light.utils.PermissionUtils
import com.flash.msc_light.utils.SpManager
import com.flash.msc_light.utils.startNotificationFlashService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFlashCloneMscAlertActivity : BaseActivity<ActivitySettingFlashAlertBinding>() {

    private val viewModel : SettingFlashAlertCloneMscViewModel by viewModels()

    private var type = ALERT_NORMAL

    companion object {
        private const val KEY_TYPE_ALERT = "KEY_TYPE_ALERT"
        const val ALERT_CALL_PHONE = "ALERT_CALL_PHONE"
        const val ALERT_NORMAL = "ALERT_NORMAL"
        const val ALERT_NOTIFICATION = "ALERT_NOTIFICATION"
        const val ALERT_SMS = "ALERT_SMS"

        fun start(activity : Activity, type : String){
            val intent = Intent(activity, SettingsFlashCloneMscAlertActivity::class.java)
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
                InterNativeUtils.showInterAction(this@SettingsFlashCloneMscAlertActivity){
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
        if(SpManager.getInstance(this).getBoolean(SpManager.can_show_ads, true)){
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
            stateLive.observe(this@SettingsFlashCloneMscAlertActivity){
                viewBinding.swStatus.isChecked = it
                startNotificationFlashService()
            }

            onTimeLive.observe(this@SettingsFlashCloneMscAlertActivity){
                viewBinding.secondAlertOnTime.text = it.toString() + " " + getString(R.string.txt_seconds)
            }
            offTimeLive.observe(this@SettingsFlashCloneMscAlertActivity){
                viewBinding.secondAlertOffTime.text = it.toString() + " " + getString(R.string.txt_seconds)
            }
            progressSbOnTimeLive.observe(this@SettingsFlashCloneMscAlertActivity){
                viewBinding.sbAlertOnTime.progress = it
            }
            progressSbOffTimeLive.observe(this@SettingsFlashCloneMscAlertActivity){
                viewBinding.sbAlertOffTime.progress = it
            }
            isTestingLive.observe(this@SettingsFlashCloneMscAlertActivity){
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
                    ivType.setImageResource(R.drawable.ic_phone_clone_msc)

                }
                ALERT_NOTIFICATION -> {
                    head.tvTitle.text = getString(R.string.txt_notification)
                    tvContentType.text = getString(R.string.txt_turn_on_for_amp_notification)
                    ivType.setImageResource(R.drawable.ic_noti_clone_msc)
                }
                ALERT_SMS -> {
                    head.tvTitle.text = getString(R.string.txt_SMS)
                    tvContentType.text = getString(R.string.txt_turn_on_for_sms)
                    ivType.setImageResource(R.drawable.ic_sms_clone_msc_clone_msc)
                }
            }
        }
    }

    override fun provideViewBinding(): ActivitySettingFlashAlertBinding {
        return ActivitySettingFlashAlertBinding.inflate(layoutInflater)
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 122){
            if(PermissionUtils.isNotificationListenerPermission(this)){
                viewBinding.swStatus.isChecked = true
            }
        }
    }
    private fun checkPermissionNotificationRead() {
        if(!PermissionUtils.isNotificationListenerPermission(this)){
            viewBinding.swStatus.isChecked = false
            showDialogPermissionNotificationRead{
                PermissionUtils.requestNotificationListenerPermission(this, 122)
            }
        }
    }
    override fun onDestroy() {
        viewModel.stopTest()
        super.onDestroy()
    }
}