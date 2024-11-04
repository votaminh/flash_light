package com.flash.light.domain.layer

open class SoundModel(val iconRes: Int, val nameRes: Int, val soundRes: Int, @Transient var selected: Boolean = false,var isPlaying : Boolean = false, var soundFlag: SoundFlag = SoundFlag.SOUND)
