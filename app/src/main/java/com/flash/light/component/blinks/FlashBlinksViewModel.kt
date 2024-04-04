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

    val stateLive = MutableLiveData(false)

    fun startSos(){
        flashHelper.startSos(context)
        stateLive.postValue(true)
    }

    fun startDJ(){
//        flashHelper.startSos(context)
        stateLive.postValue(true)
    }

    fun stop(){
        flashHelper.stop()
        stateLive.postValue(false)
    }
}