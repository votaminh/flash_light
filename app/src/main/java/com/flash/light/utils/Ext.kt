package com.flash.light.utils

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.flash.light.domain.layer.LanguageModel
import com.flash.light.domain.layer.SoundModel
import com.flash.light.domain.layer.VibrationMode
import java.util.Locale


fun isTiramisuPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

fun Context?.isPermissionGranted(permission: String?): Boolean {
    val context = this
    return if (context == null || permission.isNullOrEmpty()) false else ContextCompat.checkSelfPermission(
        context,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}

fun SoundModel.toJson(): String {
    return Gson().toJson(this)
}

fun String.toSoundModel(): SoundModel? {
    return kotlin.runCatching {
        Gson().fromJson(this, SoundModel::class.java)
    }.getOrNull()
}

fun LanguageModel.toJson(): String {
    return Gson().toJson(this)
}

fun String.toLanguageModel(): LanguageModel? {
    return kotlin.runCatching {
        Gson().fromJson(this, LanguageModel::class.java)
    }.getOrNull()
}

fun Context.setAppLanguage(languageCode: String) {
    val locale = Locale(languageCode)
    Locale.setDefault(locale)
    val config = resources.configuration
    config.setLocale(locale)
    resources.updateConfiguration(config, resources.displayMetrics)
}

fun Context.getDeviceLanguage(): String {
    return Locale.getDefault().language
}

fun Context.vibrate(vibrationMode: VibrationMode){
    kotlin.runCatching {
        val timing = when (vibrationMode) {
            VibrationMode.STRONG -> {
                longArrayOf(0, 400, 200, 400, 200, 400)
            }

            VibrationMode.HEART_BEAT -> {
                longArrayOf(0, 300, 300, 300, 600, 600, 600, 1200)
            }

            VibrationMode.TICK_TOCK -> {
                longArrayOf(0, 100, 100, 100, 100, 100, 500)
            }

            else -> {
                longArrayOf(0, 500)
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager ?: return
            val waveformEffect = VibrationEffect.createWaveform(timing, 0)
            vibratorManager.defaultVibrator.cancel()
            vibratorManager.defaultVibrator.vibrate(waveformEffect)
        } else {
            val vibrator =
                getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator ?: return
            vibrator.cancel()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val waveformEffect = VibrationEffect.createWaveform(timing, 0)
                vibrator.vibrate(waveformEffect)
            } else {
                vibrator.vibrate(timing, 0)
            }
        }
    }
}

fun Context.cancelVibrate(){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager =
            getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager ?: return
        vibratorManager.defaultVibrator.cancel()
    } else {
        val vibrator =
            getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator ?: return
        vibrator.cancel()
    }
}

fun RadioButton.changeTint(colorResEnable : Int, colorResDisable : Int){
    val colorStateList = ColorStateList(
        kotlin.arrayOf<IntArray>(
            kotlin.intArrayOf(-android.R.attr.state_enabled),
            kotlin.intArrayOf(android.R.attr.state_enabled)
        ), kotlin.intArrayOf(
            ContextCompat.getColor(context, colorResDisable),  // disabled
            ContextCompat.getColor(context, colorResEnable) // enabled
        )
    )
    buttonTintList = colorStateList // set the color tint list
    invalidate() // Could not be necessary
}