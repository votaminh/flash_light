package com.flash.light.component.main

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
class MainViewModel @Inject constructor(val spManager: SpManager, private val getListSoundUseCase: GetListSoundUseCase) : BaseViewModel() {
    private val listSound = mutableListOf<Any?>().apply {
//        this.add(0)
//        this.add(null)
    }
    val listData = MutableLiveData<List<SoundModel>>()

    fun loadSoundModels() {
        viewModelScope.launch {
            val list = getListSoundUseCase.execute(GetListSoundUseCase.Param())
            listData.postValue(list)
        }
    }

    fun saveCurrentSoundToSharedPreference(soundModel: SoundModel) {
        spManager.saveSoundModel(soundModel)
        spManager.getSoundModel()
    }

    fun getCurrentSound() : SoundModel{
        return spManager.getSoundModel()
    }

    fun getDuration() : Int{
        return spManager.getDuration()
    }

    fun saveDuration(duration : Int) {
        spManager.saveDuration(duration)
    }

    fun saveSoundModel(soundModel: SoundModel) {
        spManager.saveSoundModel(soundModel)
    }


}