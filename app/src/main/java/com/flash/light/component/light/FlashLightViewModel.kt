package com.flash.light.component.light

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import com.flash.light.utils.FlashHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class FlashLightViewModel @Inject constructor(@ApplicationContext val context: Context) : ViewModel() {
    private val flashHelper = FlashHelper()

    fun startFlash(){
        flashHelper.start(context, 100, 100)
        Handler(Looper.getMainLooper()).postDelayed({
            flashHelper.stop()
        }, 1000)
    }
}