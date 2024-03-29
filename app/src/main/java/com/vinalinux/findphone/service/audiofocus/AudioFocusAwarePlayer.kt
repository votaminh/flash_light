package com.vinalinux.findphone.service.audiofocus

interface AudioFocusAwarePlayer {
    fun isPlaying(): Boolean
    fun play()
    fun pause()
    fun stop()
    fun setVolume(volume: Float)
}