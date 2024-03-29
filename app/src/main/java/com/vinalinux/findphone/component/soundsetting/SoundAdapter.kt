package com.vinalinux.findphone.component.soundsetting

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.vinalinux.findphone.base.adapter.BaseAdapter
import com.vinalinux.findphone.databinding.ItemSongBinding
import com.vinalinux.findphone.domain.layer.SoundModel

class SoundAdapter : BaseAdapter<SoundModel, ItemSongBinding>() {
    var selectedItem: SoundModel? = null

    override fun provideViewBinding(parent: ViewGroup): ItemSongBinding {
        return ItemSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun binData(viewBinding: ItemSongBinding, item: SoundModel) {
        Log.i("dienmd", "SoundAdapter binData: ")
        viewBinding.ivIcon.setImageResource(item.iconRes)
        viewBinding.root.isSelected = item.iconRes == selectedItem?.iconRes
        viewBinding.highlightView.setOnClickListener { onClick?.invoke(item) }
    }

    fun selectItem(soundModel: SoundModel): Int {
        var index = dataSet.map { it.iconRes }.indexOf(selectedItem?.iconRes)
        selectedItem = null
        if (index > -1) {
            notifyItemChanged(index)
        }
        index = dataSet.map { it.iconRes }.indexOf(soundModel.iconRes)
        if (index > -1) {
            selectedItem = dataSet[index]
            notifyItemChanged(index)
        }
        return index
    }
}