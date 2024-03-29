package com.vinalinux.findphone.component.sound

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vinalinux.findphone.base.viewmodel.BaseViewModel
import com.vinalinux.findphone.domain.layer.SoundModel
import com.vinalinux.findphone.domain.usecase.GetListSoundUseCase
import com.vinalinux.findphone.utils.SpManager
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