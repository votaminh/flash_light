package com.flash.light.component.language

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.flash.light.base.viewmodel.BaseViewModel
import com.flash.light.domain.layer.LanguageModel
import com.flash.light.domain.usecase.GetListLanguageUseCase
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