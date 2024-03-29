package com.flash.light.component.setting

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.camera2.CameraManager
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.widget.SeekBar
import androidx.activity.result.contract.ActivityResultContracts
import com.flash.light.base.activity.BaseActivity
import com.flash.light.component.language.LanguageActivity
import com.flash.light.component.splash.SplashActivity
import com.flash.light.databinding.ActivitySettingBinding
import com.flash.light.domain.layer.FlashMode
import com.flash.light.domain.layer.VibrationMode
import com.flash.light.service.BlinkFlashRunnable
import com.flash.light.utils.DialogUtils.showDialogRequestPermissionOverlay
import com.flash.light.utils.SpManager
import com.flash.light.utils.UMPUtils
import com.flash.light.utils.cancelVibrate
import com.flash.light.utils.vibrate
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingActivity : BaseActivity<ActivitySettingBinding>() {
    companion object {
        private const val EXTRA_OPTIONS = "EXTRA_OPTIONS"
        fun getIntent(context: Context, options: String = "") = Intent(context, SettingActivity::class.java).also {
            it.putExtra(EXTRA_OPTIONS, options)
        }
    }


    private val blinkFlashHandler = Handler(Looper.getMainLooper())
    private var blinkFlashRunnable: BlinkFlashRunnable? = null

    @Inject
    lateinit var spManager: SpManager

    private val requestOverlayPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (Settings.canDrawOverlays(this)) {
                viewBinding.swWakeup.isChecked = true
                spManager.saveWakeup(true)
            }
        }

    override fun provideViewBinding(): ActivitySettingBinding =
        ActivitySettingBinding.inflate(layoutInflater)

    override fun initViews() {
        initSensitivity()
        initExtension()
        initFlashMode()
        initVibrationMode()
        updateCurrentLanguage()
        viewBinding.ivBack.setOnClickListener { finish() }
//        viewBinding.ivHelp.setOnClickListener {
//            startActivity(
//                Intent(
//                    this,
//                    HelpActivity::class.java
//                )
//            )
//        }
        viewBinding.lnLanguage.setOnClickListener {
            startActivity(Intent(this, LanguageActivity::class.java))
        }
        viewBinding.scrollView.post {
            val option = intent?.getStringExtra(EXTRA_OPTIONS) ?: ""
            if (option == "flash") {
                viewBinding.scrollView.smoothScrollTo(0, viewBinding.lnFlash.bottom)
            } else if (option == "vibration") {
                viewBinding.scrollView.smoothScrollTo(0, viewBinding.lnVibrate.bottom)
            }
        }

        if(SpManager.getInstance(this).isSettingUMPShowing()){
            viewBinding.lnUmp.visibility = View.VISIBLE
        }else{
            viewBinding.lnUmp.visibility = View.GONE
        }

        viewBinding.lnUmp.setOnClickListener {
            UMPUtils.init(this, {
                SplashActivity.start(this)
                finishAffinity()
            }, true, true)
        }
    }

    private fun initSensitivity() {
        viewBinding.seekbarSensitive.setProgress(spManager.getSensitivity())
        viewBinding.seekbarSensitive.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                spManager.saveSensitivity(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })
    }

    private fun initExtension() {
        if (!Settings.canDrawOverlays(this)) {
            spManager.saveWakeup(false)
        }
        viewBinding.swWakeup.isChecked = Settings.canDrawOverlays(this) and spManager.getWakeup()
        viewBinding.lnWakeup.setOnClickListener {
            if (Settings.canDrawOverlays(this)) {
                viewBinding.swWakeup.isChecked = !viewBinding.swWakeup.isChecked
                spManager.saveWakeup(viewBinding.swWakeup.isChecked)
            } else {
                showDialogRequestPermissionOverlay(submitAction = {
                    requestOverlayPermissionLauncher.launch(
                        Intent(
                            Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")
                        )
                    )
                })
            }
        }
    }

    private fun initFlashMode() {
        when (spManager.getFlashMode()) {
            FlashMode.DISCO -> viewBinding.checkboxDiscoMode.isChecked = true
            FlashMode.SOS -> viewBinding.checkboxSOSMode.isChecked = true
            else -> viewBinding.checkboxDefaultLight.isChecked = true
        }
        viewBinding.btnDefaultLight.setOnClickListener {
            viewBinding.checkboxDefaultLight.isChecked = true
            viewBinding.checkboxDiscoMode.isChecked = false
            viewBinding.checkboxSOSMode.isChecked = false
            spManager.saveFlashMode(FlashMode.DEFAULT)
            previewFlash(FlashMode.DEFAULT)
        }
        viewBinding.btnDiscoMode.setOnClickListener {
            viewBinding.checkboxDefaultLight.isChecked = false
            viewBinding.checkboxDiscoMode.isChecked = true
            viewBinding.checkboxSOSMode.isChecked = false
            spManager.saveFlashMode(FlashMode.DISCO)
            previewFlash(FlashMode.DISCO)
        }
        viewBinding.btnSOSMode.setOnClickListener {
            viewBinding.checkboxDefaultLight.isChecked = false
            viewBinding.checkboxDiscoMode.isChecked = false
            viewBinding.checkboxSOSMode.isChecked = true
            spManager.saveFlashMode(FlashMode.SOS)
            previewFlash(FlashMode.SOS)
        }
    }

    private fun previewFlash(flashMode : FlashMode) {

        cancelPreviewFlash()

        val isFlashAvailable = packageManager
            .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
        if (isFlashAvailable) {
            kotlin.runCatching {
                val cameraManager =
                    getSystemService(Context.CAMERA_SERVICE) as? CameraManager ?: return
                blinkFlashRunnable = BlinkFlashRunnable(blinkFlashHandler, cameraManager, flashMode)
                blinkFlashRunnable?.let {
                    blinkFlashHandler.post(it)
                }

                blinkFlashHandler.postDelayed({
                    cancelPreviewFlash()
                }, 2000)
            }
        }
    }

    private fun cancelPreviewFlash() {
        blinkFlashRunnable?.turnOffFlash()
        blinkFlashHandler.removeCallbacksAndMessages(null)
        blinkFlashRunnable = null
    }

    private fun initVibrationMode() {
        when (spManager.getVibrationMode()) {
            VibrationMode.STRONG -> viewBinding.checkboxStrongVibration.isChecked = true
            VibrationMode.HEART_BEAT -> viewBinding.checkboxHeartbeat.isChecked = true
            VibrationMode.TICK_TOCK -> viewBinding.checkboxTicktock.isChecked = true
            else -> viewBinding.checkboxDefault.isChecked = true
        }
        viewBinding.btnDefault.setOnClickListener {
            viewBinding.checkboxDefault.isChecked = true
            viewBinding.checkboxStrongVibration.isChecked = false
            viewBinding.checkboxHeartbeat.isChecked = false
            viewBinding.checkboxTicktock.isChecked = false
            spManager.saveVibrationMode(VibrationMode.DEFAULT)
            cancelVibrate()
            vibrate(VibrationMode.DEFAULT)
            Handler(Looper.getMainLooper()).postDelayed({
                if(spManager.getVibrationMode() == VibrationMode.DEFAULT){
                    cancelVibrate()
                }
            }, 2000)
        }
        viewBinding.btnStrongVibration.setOnClickListener {
            viewBinding.checkboxDefault.isChecked = false
            viewBinding.checkboxStrongVibration.isChecked = true
            viewBinding.checkboxHeartbeat.isChecked = false
            viewBinding.checkboxTicktock.isChecked = false
            spManager.saveVibrationMode(VibrationMode.STRONG)
            cancelVibrate()
            vibrate(VibrationMode.STRONG)
            Handler(Looper.getMainLooper()).postDelayed({
                if(spManager.getVibrationMode() == VibrationMode.STRONG){
                    cancelVibrate()
                }
            }, 2000)
        }
        viewBinding.btnHeartbeat.setOnClickListener {
            viewBinding.checkboxDefault.isChecked = false
            viewBinding.checkboxStrongVibration.isChecked = false
            viewBinding.checkboxHeartbeat.isChecked = true
            viewBinding.checkboxTicktock.isChecked = false
            spManager.saveVibrationMode(VibrationMode.HEART_BEAT)
            cancelVibrate()
            vibrate(VibrationMode.HEART_BEAT)
            Handler(Looper.getMainLooper()).postDelayed({
                if(spManager.getVibrationMode() == VibrationMode.HEART_BEAT){
                    cancelVibrate()
                }
            }, 2000)
        }
        viewBinding.btnTicktock.setOnClickListener {
            viewBinding.checkboxDefault.isChecked = false
            viewBinding.checkboxStrongVibration.isChecked = false
            viewBinding.checkboxHeartbeat.isChecked = false
            viewBinding.checkboxTicktock.isChecked = true
            spManager.saveVibrationMode(VibrationMode.TICK_TOCK)
            cancelVibrate()
            vibrate(VibrationMode.TICK_TOCK)
            Handler(Looper.getMainLooper()).postDelayed({
                if(spManager.getVibrationMode() == VibrationMode.TICK_TOCK){
                    cancelVibrate()
                }
            }, 2000)
        }
    }

    private fun updateCurrentLanguage() {
        viewBinding.tvLanguage.setText(spManager.getLanguage().nameRes)
    }
}