package com.vinalinux.findphone.component.main

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.marginStart
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.vinalinux.findphone.R
import com.vinalinux.findphone.base.adapter.BaseMultiTypeViewAdapter
import com.vinalinux.findphone.databinding.ItemMainAdsBinding
import com.vinalinux.findphone.databinding.ItemMainBinding
import com.vinalinux.findphone.databinding.ItemMainHeaderBinding
import com.vinalinux.findphone.domain.layer.SoundModel
import com.vinalinux.findphone.utils.SpManager

class MainAdapter(val spManager: SpManager) : BaseMultiTypeViewAdapter<Any?>() {
    companion object {
        private const val HEADER_VIEW_TYPE = 0
        private const val ITEM_VIEW_TYPE = 1
        private const val ADS_VIEW_TYPE = 2
    }

    private inner class MainHeaderViewHolder(val binding: ItemMainHeaderBinding) :
        ViewHolder(binding.root)

    private inner class MainViewHolder(val binding: ItemMainBinding) : ViewHolder(binding.root)
    private inner class AdsViewHolder(val binding: ItemMainAdsBinding) : ViewHolder(binding.root)

    override fun getViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            HEADER_VIEW_TYPE -> MainHeaderViewHolder(
                ItemMainHeaderBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )

            ADS_VIEW_TYPE -> AdsViewHolder(
                ItemMainAdsBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )

            else -> MainViewHolder(
                ItemMainBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
        }
    }

    override fun getViewType(position: Int): Int {
        return when (dataSet[position]) {
            !is SoundModel -> ADS_VIEW_TYPE
            else -> ITEM_VIEW_TYPE
        }
    }

    override fun binData(viewHolder: ViewHolder, item: Any?, position: Int) {
        when (viewHolder) {
            is MainHeaderViewHolder -> {
                val soundModel = spManager.getSoundModel()
                viewHolder.binding.ivIcon.setImageResource(soundModel.iconRes)
                viewHolder.binding.tvName.setText(soundModel.nameRes)
                viewHolder.binding.root.setOnClickListener {
                    onClick?.invoke(item)
                }
            }

            is MainViewHolder -> {
                if (item is SoundModel) {
//                    val drawableRes = when (position) {
//                        1 -> R.drawable.shape_rectangle_white_top_left_rad_20
//                        3 -> R.drawable.shape_rectangle_white_top_right_rad_20
//                        else -> R.drawable.shape_rectangle_white
//                    }
//                    viewHolder.binding.root.setBackgroundResource(drawableRes)
                    viewHolder.binding.ivIcon.setImageResource(item.iconRes)
                    viewHolder.binding.tvName.setText(item.nameRes)
                    viewHolder.binding.layoutIcon.isSelected = item.selected
                    viewHolder.binding.root.setOnClickListener {
                        onClick?.invoke(item)
                    }
                }
            }

            is AdsViewHolder -> {
                viewHolder.binding.root.layoutParams.height =
                    if (item == null) 0 else ViewGroup.LayoutParams.WRAP_CONTENT
            }
        }
    }

    fun isItemView(position: Int): Boolean {
        return getViewType(position) == ITEM_VIEW_TYPE
    }

    fun selectItem(soundModel: SoundModel) {
        var index = dataSet.map { if (it is SoundModel) it.selected else false }.indexOf(true)
        if (index > -1) {
            (dataSet[index] as? SoundModel)?.selected = false
            notifyItemChanged(index)
        }
        index = dataSet.map { if (it is SoundModel) it.iconRes else -1 }.indexOf(soundModel.iconRes)
        if (index > -1) {
            (dataSet[index] as? SoundModel)?.selected = true
            notifyItemChanged(index)
        }
    }

    fun getSoundSelectedItem(): SoundModel? = dataSet.filterIsInstance<SoundModel>().find { it.selected }
}