package com.flash.msc_light.domain.usecase

import com.flash.msc_light.R
import com.flash.msc_light.domain.layer.LanguageModel
import javax.inject.Inject

class GetListLanguageUseCase @Inject constructor() :
    UseCase<GetListLanguageUseCase.Param, List<LanguageModel>>() {

    open class Param() : UseCase.Param()

    override suspend fun execute(param: Param): List<LanguageModel> = listOf(
        LanguageModel("vi", R.drawable.ic_vietnamese_clone_msc, R.string.vietnamese),
        LanguageModel("hi", R.drawable.ic_hindi_clone_msc, R.string.hindi),
        LanguageModel("es", R.drawable.ic_spanish_clone_msc, R.string.spanish),
        LanguageModel("en", R.drawable.ic_english_clone_msc, R.string.english),
        LanguageModel("fr", R.drawable.ic_france_clone_msc, R.string.french),
        LanguageModel("pt", R.drawable.ic_portugal_clone_msc, R.string.portuguese)
    )
}