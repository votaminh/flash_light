package com.flash.light.dialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.flash.light.databinding.DialogPermisisonNotificationReadBinding

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
}