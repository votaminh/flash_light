package com.flash.light.utils

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import androidx.preference.PreferenceManager


class FlashHelper {
    private var cameraManager : CameraManager? = null
    private var idCamera = ""
    private var endFlash = true

    companion object {
        private var instance: FlashHelper? = null

        fun getInstance(): FlashHelper {
            if (instance == null) {
                instance = FlashHelper()
            }
            return instance!!
        }
    }

    fun start(context : Context, turnOnTime : Long, turnOffTime : Long){
        if(!endFlash){
            return
        }
        endFlash = false
        Thread{
            val isFlashAvailable = context.packageManager?.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
            if (isFlashAvailable == true) {
                cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as? CameraManager
                cameraManager?.run {
                    getCameraIdSupportFlash(this)?.let {
                        idCamera = it
                        while (!endFlash){
                            setTorchMode(it, true)
                            Thread.sleep(turnOnTime)
                            setTorchMode(it, false)
                            Thread.sleep(turnOffTime)
                        }
                    }
                }
            }
        }.start()
    }
    private fun getCameraIdSupportFlash(cameraManager : CameraManager): String? {
        return cameraManager.cameraIdList.firstNotNullOfOrNull {
            if (cameraManager.getCameraCharacteristics(it)
                    .get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
            ) it else null
        }
    }

    fun stop() {
        endFlash = true
        cameraManager?.run {
            setTorchMode(idCamera, false)
        }
    }
}