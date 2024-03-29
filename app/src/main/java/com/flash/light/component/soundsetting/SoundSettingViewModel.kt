package com.flash.light.component.soundsetting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.flash.light.base.viewmodel.BaseViewModel
import com.flash.light.domain.layer.SoundModel
import com.flash.light.domain.usecase.GetListSoundUseCase
import com.flash.light.utils.SpManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SoundSettingViewModel @Inject constructor(private val getListSoundUseCase: GetListSoundUseCase, private val spManager: SpManager) : BaseViewModel() {
    val listSound = MutableLiveData<List<SoundModel>>()

    fun loadSoundModel() {
        viewModelScope.launch {
            listSound.value = getListSoundUseCase.execute(GetListSoundUseCase.Param())
        }
    }

    fun getDuration(): Int = spManager.getDuration()

    fun saveDuration(duration: Int) {
        spManager.saveDuration(duration)
    }

    fun saveSoundModel(soundModel: SoundModel) {
        spManager.saveSoundModel(soundModel)
    }

    fun saveFlash(enable: Boolean) {
        spManager.saveFlash(enable)
    }

    fun saveVibration(enable: Boolean) {
        spManager.saveVibration(enable)
    }

    fun saveEnableSound(enable: Boolean) {
        spManager.saveEnableVolume(enable)
    }

    fun saveVolume(volume: Int) {
        spManager.saveVolume(volume)
    }

    fun getFlash(): Boolean = spManager.getFlash()
    fun getVibration(): Boolean = spManager.getVibration()
    fun getEnableVolume(): Boolean = spManager.getEnableVolume()
    fun getVolume(): Int = spManager.getVolume()
    fun getSoundModel(): SoundModel {
        return spManager.getSoundModel()
    }
}