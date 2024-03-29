package com.flash.light.component.permission

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.flash.light.R
import com.flash.light.base.activity.BaseActivity
import com.flash.light.component.main.MainActivity
import com.flash.light.databinding.ActivityPermissionBinding
//import com.flash.light.utils.isTiramisuPlus

class PermissionActivity : BaseActivity<ActivityPermissionBinding>() {
    companion object {
        private const val EXTRA_PERMISSION = "EXTRA_PERMISSION"

        fun getIntent(context: Context, permission: String = Manifest.permission.RECORD_AUDIO) =
            Intent(context, PermissionActivity::class.java).also {
                it.putExtra(EXTRA_PERMISSION, permission)
            }
    }
    private lateinit var mAskCheckPermissionStorage: ActivityResultLauncher<Array<String>>

    private val listPermission by lazy {
//        if (isTiramisuPlus()) {
//            arrayOf(Manifest.permission.RECORD_AUDIO , Manifest.permission.POST_NOTIFICATIONS)
//        } else {
//            arrayOf(Manifest.permission.RECORD_AUDIO)
//        }
    }

    override fun provideViewBinding(): ActivityPermissionBinding =
        ActivityPermissionBinding.inflate(layoutInflater)

//    private val requestPermissionLauncher =
//        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
//            var shouldShowPermissionSetting = false
//            var allGranted = true
//            result.entries.forEach {
//                when (it.key) {
//                    Manifest.permission.RECORD_AUDIO, Manifest.permission.POST_NOTIFICATIONS -> {
//                        if (!it.value) {
//                            allGranted = false
//                            if (ActivityCompat.shouldShowRequestPermissionRationale(
//                                    this,
//                                    it.key
//                                )
//                            ) {
//                                // denied without never show again
//                            } else {
//                                // denied never show again
//                                shouldShowPermissionSetting = true
//                            }
//                            if (Manifest.permission.RECORD_AUDIO == it.key) {
//                                viewBinding.swAudioRecord.isChecked = false
//                                updateNextButtonStatus()
//                            } else if (Manifest.permission.POST_NOTIFICATIONS == it.key) {
//                                viewBinding.swNotification.isChecked = false
//                                updateNextButtonStatus()
//                            }
//                        }
//                    }
//                }
//            }
//            if (shouldShowPermissionSetting) {
//                showDialogRequestPermissionFromSettings()
//            }
//            if (allGranted) {
//                finish()
//            }
//        }

//    private val requestPermissionLauncher =
//    registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
//        if (result.entries.firstOrNull { !it.value } == null) {
//            finish()
//        } else {
//            if (needRequiredOpenSettingPermission()) {
//                showDialogRequestPermissionFromSettings()
//            } else {
//
//            }
//        }
//    }

//    private fun needRequiredOpenSettingPermission(): Boolean {
//        listPermission.forEach { permission ->
//            if (!ActivityCompat.shouldShowRequestPermissionRationale(
//                    this,
//                    permission
//                )
//            ) return true
//        }
//        return false
//    }
//
//    private val settingPermissionContract =
//        registerForActivityResult(SettingPermissionResultContract()) {
//            if (Utils.isPermissionGranted(this, Manifest.permission.RECORD_AUDIO)) {
//                viewBinding.swAudioRecord.isChecked = false
//                updateNextButtonStatus()
//            } else if (Utils.isPermissionGranted(this, Manifest.permission.POST_NOTIFICATIONS)) {
//                viewBinding.swNotification.isChecked = false
//                updateNextButtonStatus()
//            }
//        }
//
//    override fun initViews() {
//
//        mAskCheckPermissionStorage =
//            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
//                if (result.entries.firstOrNull { !it.value } == null) {
//                    Logger.e("success")
//                    MainActivity.start(this@PermissionActivity)
//                    finish()
//                } else {
//                    if (needRequiredOpenSettingPermission()) {
//                        showDialogRequestPermissionFromSettings()
//                        Logger.e("onPermissionDenyForever")
//                    } else {
//                        showDialogRequestPermissionFromSettings()
//                        Logger.e("onPermissionDeny")
//                    }
//                }
//            }
//
//        viewBinding.ivBack.setOnClickListener {
//            onBackPressedDispatcher.onBackPressed()
//        }
//        viewBinding.layoutNotification.isVisible = isTiramisuPlus() && !Utils.isPermissionGranted(this, Manifest.permission.POST_NOTIFICATIONS)
//        viewBinding.layoutAudioRecord.setOnClickListener {
//            viewBinding.swAudioRecord.isChecked = !viewBinding.swAudioRecord.isChecked
//            updateNextButtonStatus()
//        }
//        viewBinding.layoutNotification.setOnClickListener {
//            viewBinding.swNotification.isChecked = !viewBinding.swNotification.isChecked
//            updateNextButtonStatus()
//        }
//        viewBinding.tvNext.setOnClickListener {
//            requestPermission()
//        }
//    }
//
//    private fun updateStateChecked() {
//        viewBinding.swAudioRecord.isChecked = Utils.isPermissionGranted(this, Manifest.permission.RECORD_AUDIO)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            viewBinding.swNotification.isChecked = Utils.isPermissionGranted(this, Manifest.permission.POST_NOTIFICATIONS)
//        }
//    }
//
//
//    override fun onResume() {
//        super.onResume()
//        updateNextButtonStatus()
//        updateStateChecked()
//    }
//
//    private fun updateNextButtonStatus() {
//        val isNotificationGranted = if (isTiramisuPlus() && !Utils.isPermissionGranted(this, Manifest.permission.POST_NOTIFICATIONS)) {
//            viewBinding.swNotification.isChecked
//        } else true
//        viewBinding.tvNext.isEnabled = viewBinding.swAudioRecord.isChecked && isNotificationGranted
//        if (viewBinding.tvNext.isEnabled) {
//            viewBinding.tvNext.setTextColor(ContextCompat.getColor(this , R.color.orange))
//        } else {
//            viewBinding.tvNext.setTextColor(ContextCompat.getColor(this , R.color.colorDisable))
//        }
//    }
//
//    private fun requestPermission() {
//        listPermission.forEach {
//            Logger.e(it)
//        }
////        val permissions = mutableListOf(Manifest.permission.RECORD_AUDIO)
////        if (isTiramisuPlus()) {
////            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
////        }
////        requestPermissionLauncher.launch(permissions.toTypedArray())
//
//        mAskCheckPermissionStorage.launch(listPermission)
//    }
//
//    private fun showDialogRequestPermissionFromSettings() {
//        showDialogGuidePermissionSetting(
//            title = getTitleDialogGuidePermissionSetting(),
//            description = getDescriptionDialogGuidePermissionSetting(),
//            submitAction = {
//                openSettingsToGrantPermission()
//                DialogUtils.dismissDialog()
//            }
//        )
//    }
//
//    private fun getTitleDialogGuidePermissionSetting(): String {
//        return getString(R.string.txt_open_settings_permission_title)
//    }
//
//    private fun getDescriptionDialogGuidePermissionSetting(): String {
//        return getString(R.string.txt_open_settings_message_permission)
//    }
//
//    private fun permissionGranted(permission: String, alreadyGranted: Boolean = false) {
//        finish()
//    }
//
//    private fun openSettingsToGrantPermission() {
//        settingPermissionContract.runCatching {
//            settingPermissionContract.launch(0)
//        }.onFailure {
//            //Toast.makeText(this, getString(R.string.txt_no_activity_camera), Toast.LENGTH_SHORT).show()
//        }
//    }
}