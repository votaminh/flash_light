package com.flash.light.utils.flash

class FlashRunnable constructor(
    private val turnOnTime : Long,
    private val turnOffTime : Long,
    private val timeOff : Long? = null,
    private val turnOnAction : (() -> Unit)? = null,
    private val turnOffAction : (() -> Unit)? = null,
    private val endAction : (() -> Unit)? = null) : Runnable {

    var endFlash = false
    var timeStart = 0L
    var timeEnd = 0L

    override fun run() {
        timeStart = System.currentTimeMillis()
        if(timeOff != null){
            timeEnd = timeStart + timeOff
        }
        endFlash = false
        while (!endFlash){
            if(timeOff != null && System.currentTimeMillis() > timeEnd){
                endAction?.invoke()
                break
            }

            turnOnAction?.invoke()
            if(endFlash){
                break
            }
            Thread.sleep(turnOnTime)
            if(endFlash){
                break
            }
            turnOffAction?.invoke()
            if(endFlash){
                break
            }
            Thread.sleep(turnOffTime)
            if(endFlash){
                break
            }
        }
    }
}