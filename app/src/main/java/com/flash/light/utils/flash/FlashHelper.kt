package com.flash.light.utils.flash

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager


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

    fun startNormal(context : Context, turnOnTime : Long, turnOffTime : Long){
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

    fun startSos(context: Context){
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
                            Thread.sleep(100)
                            setTorchMode(it, false)
                            Thread.sleep(100)
                            setTorchMode(it, true)
                            Thread.sleep(100)
                            setTorchMode(it, false)
                            Thread.sleep(100)
                            setTorchMode(it, true)
                            Thread.sleep(100)
                            setTorchMode(it, false)
                            Thread.sleep(300)

                            setTorchMode(it, true)
                            Thread.sleep(300)
                            setTorchMode(it, false)
                            Thread.sleep(300)
                            setTorchMode(it, true)
                            Thread.sleep(300)
                            setTorchMode(it, false)
                            Thread.sleep(300)
                            setTorchMode(it, true)
                            Thread.sleep(300)
                            setTorchMode(it, false)
                            Thread.sleep(300)

                            setTorchMode(it, true)
                            Thread.sleep(100)
                            setTorchMode(it, false)
                            Thread.sleep(100)
                            setTorchMode(it, true)
                            Thread.sleep(100)
                            setTorchMode(it, false)
                            Thread.sleep(100)
                            setTorchMode(it, true)
                            Thread.sleep(100)
                            setTorchMode(it, false)
                            Thread.sleep(2000)
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