package com.vinalinux.findphone.dialog

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AlertDialog
import com.vinalinux.findphone.databinding.DialogLoadingAdsBinding

class DialogShowAdsLoading(context: Context) : AlertDialog(context) {

    var onDismissDialog: (() -> Unit)? = null

    init {
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        setCancelable(false)
    }

    private lateinit var viewBinding: DialogLoadingAdsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DialogLoadingAdsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }

    fun showDialog(delay: Long = 500L) {
        if (!isShowing) {
            show()
            Handler(Looper.getMainLooper()).postDelayed({
                dismiss()
                onDismissDialog?.invoke()
            }, delay)
        }
    }
}