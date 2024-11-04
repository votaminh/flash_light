package com.flash.msc_light.component.feature

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.flash.msc_light.utils.flash.FlashHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class FlashBlinksCloneMscViewModel @Inject constructor(@ApplicationContext private val context: Context) : ViewModel() {
    @Inject
    lateinit var flashHelper: FlashHelper

    private var detectorSoundCloneMscThread : DetectorSoundCloneMscThread? = null

    val stateLive = MutableLiveData(false)

    fun startSos(){
        flashHelper.startSos(context)
        stateLive.postValue(true)
    }

    fun startDJ(){
        stateLive.postValue(true)
        if(detectorSoundCloneMscThread == null){
            detectorSoundCloneMscThread =
                DetectorSoundCloneMscThread(
                    context
                )
        }
        detectorSoundCloneMscThread?.run {
            setOnSoundListener(object :
                DetectorSoundCloneMscThread.OnSoundListener {
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

        detectorSoundCloneMscThread?.run {
            setOnSoundListener(null)
            stopDetection()
        }

        stateLive.postValue(false)
    }

}