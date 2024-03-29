package com.flash.light.component.sound

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.flash.light.R
import com.flash.light.admob.NativeAdmob
import com.flash.light.base.adapter.BaseAdapter
import com.flash.light.databinding.ItemSoundBinding
import com.flash.light.domain.layer.SoundFlag
import com.flash.light.domain.layer.SoundModel
import com.flash.light.utils.changeTint

class SoundAdapter : BaseAdapter<SoundModel, ItemSoundBinding>() {

    var soundClick : ((SoundModel, Int) -> Unit)? = null
    var playSound : ((SoundModel, Int) -> Unit)? = null
    var moreSetting : ((SoundModel, Int) -> Unit)? = null

    var preSelect = -1
    var selected = -1

    var nativeAdmob : NativeAdmob? = null

    override fun provideViewBinding(parent: ViewGroup): ItemSoundBinding {
        return ItemSoundBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun binData(viewBinding: ItemSoundBinding, item: SoundModel) {
        with(viewBinding){
            if(item.soundFlag == SoundFlag.SOUND){

                llItem.visibility = View.VISIBLE
                flAdplaceholder.visibility = View.GONE

                imvIcon.setImageResource(item.iconRes)
                tvTitle.setText(item.nameRes)

                var currentPossion = getListData().indexOf(item)

                if(selected == currentPossion){
                    ratioTick.isChecked = true

                    ratioTick.changeTint(R.color.main, R.color.gray4)
                    root.setBackgroundResource(R.drawable.bg_stroke1_main__round10)
                }else{
                    ratioTick.isChecked = false
                    ratioTick.changeTint(R.color.gray4, R.color.gray4)
                    root.setBackgroundResource(R.drawable.bg_gray05_stroke1_gray1_round10)
                }

                ratioTick.isChecked = selected == currentPossion

                root.setOnClickListener {
                    soundClick?.invoke(item, currentPossion)
                }

                play.setOnClickListener {
                    playSound?.invoke(item, currentPossion)
                }

                more.setOnClickListener {
                    moreSetting?.invoke(item, currentPossion)
                }

                if(item.isPlaying){
                    imvPlay.setImageResource(R.drawable.ic_pause_sound)
                }else{
                    imvPlay.setImageResource(R.drawable.ic_play_sound)
                }
            }else{
                llItem.visibility = View.GONE
                flAdplaceholder.visibility = View.VISIBLE

                nativeAdmob?.run {
                    showNative(flAdplaceholder, null)
                }
            }
        }
    }

    fun setSelect(i : Int){
        preSelect = selected
        selected = i
        notifyItemChanged(preSelect)
        notifyItemChanged(selected)
    }
}