package com.vinalinux.findphone.component.language

import android.view.LayoutInflater
import android.view.ViewGroup
import com.vinalinux.findphone.base.adapter.BaseAdapter
import com.vinalinux.findphone.databinding.ItemLanguageBinding
import com.vinalinux.findphone.domain.layer.LanguageModel

class LanguageAdapter : BaseAdapter<LanguageModel, ItemLanguageBinding>() {
    override fun provideViewBinding(parent: ViewGroup): ItemLanguageBinding {
        return ItemLanguageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun binData(viewBinding: ItemLanguageBinding, item: LanguageModel) {
        viewBinding.imgLanguage.setImageResource(item.iconRes)
        viewBinding.tvTitleLanguage.setText(item.nameRes)
        viewBinding.swLanguage.isChecked = item.selected
        viewBinding.root.setOnClickListener {
            onClick?.invoke(item)
        }
    }

    fun selectLanguage(languageCode: String) {
        var index = dataSet.indexOfFirst { it.selected }
        if (index > -1) {
            dataSet[index].selected = false
            notifyItemChanged(index)
        }
        index = dataSet.indexOfFirst { it.languageCode == languageCode }
        if (index > -1) {
            dataSet[index].selected = true
            notifyItemChanged(index)
        }
    }

    fun selectedLanguage(): LanguageModel? = dataSet.firstOrNull { it.selected }
}