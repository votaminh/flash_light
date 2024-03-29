package com.vinalinux.findphone.service

import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Handler
import com.vinalinux.findphone.domain.layer.FlashMode

class BlinkFlashRunnable(private val handler: Handler, private val cameraManager: CameraManager, flashMode: FlashMode) :
    Runnable {
    private val flashMode = when (flashMode) {
        FlashMode.DISCO -> arrayOf("10", "10")
        FlashMode.SOS -> arrayOf("101010", "10")
        else -> arrayOf("1")
    }
    private var sleepTime = 100L
    private var delayTime = 300L
    private var currentModeIndex = 0

    override fun run() {
        kotlin.runCatching {
            getCameraIdSupportFlash()?.let { id ->
                currentModeIndex = currentModeIndex.mod(flashMode.size)
                flashMode[currentModeIndex].forEach {
                    cameraManager.setTorchMode(id, it == '1')
                    Thread.sleep(sleepTime)
                }
                currentModeIndex++
            }
        }
        handler.postDelayed(this, delayTime)
    }

    fun turnOffFlash() {
        getCameraIdSupportFlash()?.let { id ->
            cameraManager.setTorchMode(id, false)
        }
    }

    private fun getCameraIdSupportFlash(): String? {
        return cameraManager.cameraIdList.firstNotNullOfOrNull {
            if (cameraManager.getCameraCharacteristics(it)
                    .get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
            ) it else null
        }
    }
}