package com.vinalinux.findphone.component.language

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vinalinux.findphone.base.viewmodel.BaseViewModel
import com.vinalinux.findphone.domain.layer.LanguageModel
import com.vinalinux.findphone.domain.usecase.GetListLanguageUseCase
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