package com.flash.msc_light.component.language

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.flash.msc_light.base.viewmodel.BaseViewModel
import com.flash.msc_light.domain.layer.LanguageModel
import com.flash.msc_light.domain.usecase.GetListLanguageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(private val getListLanguageUseCase: GetListLanguageUseCase) : BaseViewModel() {
    val listLanguage = MutableLiveData<List<LanguageModel>>()
    fun loadListLanguage() {
        viewModelScope.launch {
            listLanguage.value = getListLanguageUseCase.execute(GetListLanguageUseCase.Param())
        }
    }
}