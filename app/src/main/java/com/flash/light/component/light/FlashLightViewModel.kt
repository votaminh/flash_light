package com.flash.light.component.light

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.flash.light.utils.FlashHelper
import com.flash.light.utils.SpManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class FlashLightViewModel @Inject constructor(@ApplicationContext val context: Context) : ViewModel() {

    @Inject
    lateinit var spManager : SpManager
    private val flashHelper = FlashHelper()

    val isFlashTurnOn = MutableLiveData<Boolean>()

    fun startFlash(){
        val timeTurnOn = spManager.getTurnOnTimeMs()
        val timeTurnOff = spManager.getTurnOffTimeMs()
        flashHelper.start(context, timeTurnOn, timeTurnOff)
        isFlashTurnOn.postValue(true)
    }

    fun stopFlash(){
        isFlashTurnOn.postValue(false)
        flashHelper.stop()
    }
}