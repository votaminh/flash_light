package com.flash.light.dialog

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.flash.light.admob.NameRemoteAdmob
import com.flash.light.databinding.DialogExitBinding
import com.flash.light.databinding.DialogPermisisonNotificationReadBinding
import com.flash.light.utils.NativeAdmobUtils

object DialogExt {
    fun Context.showDialogPermissionNotificationRead(ok : (() -> Unit)? = null){
        val builder = AlertDialog.Builder(this)
        val binding = DialogPermisisonNotificationReadBinding.inflate(LayoutInflater.from(this))
        builder.setView(binding.root)
        val dialog = builder.create()
        dialog.show()

        binding.tvOk.setOnClickListener {
            ok?.invoke()
            dialog.dismiss()
        }

    }

    fun Activity?.showDialogExit(
        lifecycle: LifecycleOwner,
        exitAction : (() -> Unit) ? = null
    ){
        val builder = AlertDialog.Builder(this)
        val binding = DialogExitBinding.inflate(LayoutInflater.from(this))
        builder.setView(binding.root)
        val dialog = builder.create()
        dialog.show()

        NativeAdmobUtils.nativeExitLiveData?.run {
            nativeAdLive?.observe(lifecycle){
                if(available()/* && spManager.getBoolean(NameRemoteAdmob.NATIVE_LANGUAGE, true)*/){
                    binding.flAdplaceholder.visibility = View.VISIBLE
                    showNative(binding.flAdplaceholder, null)
                }
            }
        }

        with(binding){
            tvExit.setOnClickListener {
                exitAction?.invoke()
            }
            tvCancel.setOnClickListener {
                dialog.dismiss()
            }
        }
    }
}