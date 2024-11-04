package com.flash.msc_light.domain.layer

data class LanguageModel(
    val languageCode: String,
    val iconRes: Int,
    val nameRes: Int,
    var selected: Boolean = false
)