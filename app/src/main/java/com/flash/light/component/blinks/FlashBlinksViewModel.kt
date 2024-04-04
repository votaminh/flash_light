package com.flash.light.component.blinks

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.flash.light.utils.flash.FlashHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class FlashBlinksViewModel @Inject constructor(@ApplicationContext private val context: Context) : ViewModel() {
    @Inject
    lateinit var flashHelper: FlashHelper

    private var detectorSoundThread : DetectorSoundThread? = null

    val stateLive = MutableLiveData(false)

    init {
        detectorSoundThread = DetectorSoundThread(context)
    }

    fun startSos(){
        flashHelper.startSos(context)
        stateLive.postValue(true)
    }

    fun startDJ(){
        stateLive.postValue(true)
        detectorSoundThread?.run {
            setOnSoundListener(object : DetectorSoundThread.OnSoundListener{
                override fun onDetectLowAmplitudeSound() {
                    flashHelper.stop()
                }

                override fun onDetectSuccessSound() {
                    flashHelper.toggleFlash(context)
                }
            })
            start()
        }
    }

    fun stop(){
        flashHelper.stop()

        detectorSoundThread?.run {
            setOnSoundListener(null)
            stopDetection()
        }

        stateLive.postValue(false)
    }
}