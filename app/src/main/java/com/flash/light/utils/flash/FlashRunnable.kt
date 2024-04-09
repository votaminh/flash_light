package com.flash.light.utils.flash

class FlashRunnable constructor(
    private val turnOnTime : Long,
    private val turnOffTime : Long,
    private val turnOnAction : (() -> Unit)? = null,
    private val turnOffAction : (() -> Unit)? = null ) : Runnable {

    var endFlash = false

    override fun run() {
        endFlash = false
        while (!endFlash){
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