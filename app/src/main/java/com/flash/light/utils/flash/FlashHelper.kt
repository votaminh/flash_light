package com.flash.light.utils.flash

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.media.AudioManager
import android.util.Log
import com.flash.light.utils.AppUtils
import com.flash.light.utils.SpManager


class FlashHelper {
    private var cameraManager : CameraManager? = null
    private var idCamera = ""
    private var currentTorchMode = false
    private var flashRunnable : FlashRunnable? = null
    private var endFlash = false

    companion object {
        private val TAG = "FlashHelper"
        private var instance: FlashHelper? = null

        fun getInstance(): FlashHelper {
            Log.i(TAG, "getInstance: ")
            if (instance == null) {
                instance = FlashHelper()
            }
            return instance!!
        }
    }

    fun startNormal(context : Context, turnOnTime : Long, turnOffTime : Long, showRequest : Boolean = false, timeEnd : Long? = null){
        Log.i(TAG, "startNormal: ")
        val spManager = SpManager.getInstance(context)

        if(!showRequest){
            val notFlashWhenScreenOn = spManager.getNotFlashWhenScreenOn()
            if(notFlashWhenScreenOn){
                if(AppUtils.isScreenOn(context)){
                    return
                }
            }

            val notFlashWhenBatteryLow = spManager.getNotFlashWhenBatteryLow()
            if(notFlashWhenBatteryLow){
                if(AppUtils.isLowBattery(context)){
                    return
                }
            }
        }

        val am = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager?
        var canShow = true
        am?.let {
            when (it.ringerMode) {
                AudioManager.RINGER_MODE_SILENT -> {
                    canShow = spManager.getFlashInSilentMode()
                }
                AudioManager.RINGER_MODE_VIBRATE -> {
                    canShow = spManager.getFlashInVibrateMode()
                }
                AudioManager.RINGER_MODE_NORMAL -> {
                    canShow = spManager.getFlashInNormalMode()
                }
                else -> {}
            }
        }

        if(!canShow){
            return
        }

        flashRunnable?.let {
            if(it.endFlash){
                return
            }
        }

        Thread{
            val isFlashAvailable = context.packageManager?.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
            if (isFlashAvailable == true) {
                cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as? CameraManager
                cameraManager?.run {
                    getCameraIdSupportFlash(this)?.let {
                        idCamera = it
                        flashRunnable = FlashRunnable(turnOnTime, turnOffTime, timeEnd,
                            turnOnAction = {
                            setTorchMode(it, true)
                        }, turnOffAction = {
                            setTorchMode(it, false)
                        }, endAction = {
                            stop()
                        })
                        flashRunnable?.run()
                    }
                }
            }
        }.start()
    }

    fun startSos(context: Context){
        Log.i(TAG, "startSos: ")
        if(!endFlash){
            return
        }
        endFlash = false
        Thread{
            val dataset = "1,0,1,0,1,0,,,1,,,0,,,1,,,0,,,1,,,0,,,1,0,1,0,1,0"

            val isFlashAvailable = context.packageManager?.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
            if (isFlashAvailable == true) {
                cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as? CameraManager
                cameraManager?.run {
                    getCameraIdSupportFlash(this)?.let {
                        idCamera = it
                        while (!endFlash){

                            for(i in dataset.indices){
                                if(endFlash){
                                    break
                                }
                                val data = dataset[i]
                                if(data == '1'){
                                    setTorchMode(it, true)
                                }else if(data == '0'){
                                    setTorchMode(it, false)
                                }else if(data == ','){
                                    Thread.sleep(100)
                                }
                            }
                            if(endFlash){
                                break
                            }
                            Thread.sleep(1000)
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
        flashRunnable?.let {
            it.endFlash = true
        }
        flashRunnable = null
        cameraManager?.run {
            setTorchMode(idCamera, false)
        }
    }

    fun toggleFlash(context: Context) {
        Log.i(TAG, "toggleFlash: ")
        if(!endFlash){
            return
        }
        cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as? CameraManager
        cameraManager?.run {
            getCameraIdSupportFlash(this)?.let {
                idCamera = it
                setTorchMode(it, currentTorchMode)
                currentTorchMode = !currentTorchMode
            }
        }
    }
}