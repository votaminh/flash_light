package com.flash.light.component.sound

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
class SoundViewModel @Inject constructor(val spManager: SpManager, private val getListSoundUseCase: GetListSoundUseCase) : BaseViewModel() {
    val listData = MutableLiveData<List<SoundModel>>()

    fun loadSoundModels() {
        viewModelScope.launch {
            val list = getListSoundUseCase.execute(GetListSoundUseCase.Param())
//            listSound.addAll(1, list.take(3))
//            listSound.addAll(list.drop(3))
            listData.value = list
        }
    }
}