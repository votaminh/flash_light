package com.vinalinux.findphone.domain.layer

import com.vinalinux.findphone.R

open class SoundModel(val iconRes: Int, val nameRes: Int, val soundRes: Int, @Transient var selected: Boolean = false,var isPlaying : Boolean = false, var soundFlag: SoundFlag = SoundFlag.SOUND)
