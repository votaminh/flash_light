package com.vinalinux.findphone.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.vinalinux.findphone.BuildConfig
import com.vinalinux.findphone.databinding.DialogConfirmExitBinding
import com.vinalinux.findphone.databinding.DialogGuidePermissionSettingBinding
import com.vinalinux.findphone.databinding.DialogRequestPermissionOverlayBinding

object DialogUtils {
    var showingDialog: Dialog? = null

    fun Context?.showDialogGuidePermissionSetting(
        title: String?,
        description: String?,
        submitAction: (() -> Unit)? = null,
        cancelAction: () -> Unit = {},
        cancelable: Boolean = true
    ): AlertDialog? {
        val context = this ?: return null
        return MaterialAlertDialogBuilder(context).apply {
            val binding = DialogGuidePermissionSettingBinding.inflate(LayoutInflater.from(context))
            val view = binding.root
            setView(view)
            title?.let {
                binding.txtDialogTitle.text = it
            }
            description?.let {
                binding.txtDialogMessage.text = it
            }
            binding.btnDialogConfirmYes.setOnClickListener {
                submitAction?.invoke()
            }
            setCancelable(cancelable)
        }.create().apply {
            if (showingDialog?.isShowing == true) {
                showingDialog?.dismiss()
            }
            if (context is LifecycleOwner) {
                context.lifecycle.addObserver(object : DefaultLifecycleObserver {
                    override fun onDestroy(owner: LifecycleOwner) {
                        this@apply.dismiss()
                        if (showingDialog === this@apply) {
                            showingDialog = null
                        }
                        super.onDestroy(owner)
                    }
                })
            }
            showingDialog = this
            show()
        }
    }

    fun Context?.showDialogRequestPermissionOverlay(
        submitAction: (() -> Unit)? = null,
        cancelAction: () -> Unit = {},
        cancelable: Boolean = true
    ): AlertDialog? {
        val context = this ?: return null
        return MaterialAlertDialogBuilder(context).apply {
            val binding =
                DialogRequestPermissionOverlayBinding.inflate(LayoutInflater.from(context))
            val view = binding.root
            setView(view)
            binding.tvYes.setOnClickListener {
                submitAction?.invoke()
                dismissDialog()
            }
            binding.tvNo.setOnClickListener {
                dismissDialog()
            }
            setCancelable(cancelable)
        }.create().apply {
            if (showingDialog?.isShowing == true) {
                showingDialog?.dismiss()
            }
            if (context is LifecycleOwner) {
                context.lifecycle.addObserver(object : DefaultLifecycleObserver {
                    override fun onDestroy(owner: LifecycleOwner) {
                        this@apply.dismiss()
                        if (showingDialog === this@apply) {
                            showingDialog = null
                        }
                        super.onDestroy(owner)
                    }
                })
            }
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            showingDialog = this
            show()
        }
    }

    fun Context?.showDialogConfirmExit(
        submitAction: (() -> Unit)? = null,
        cancelAction: () -> Unit = {},
        cancelable: Boolean = true
    ): AlertDialog? {
        val context = this ?: return null
        return MaterialAlertDialogBuilder(context).apply {
            val binding = DialogConfirmExitBinding.inflate(LayoutInflater.from(context))
            val view = binding.root
            setView(view)
            binding.tvYes.setOnClickListener {
                submitAction?.invoke()
                dismissDialog()
            }
            binding.tvNo.setOnClickListener {
                cancelAction.invoke()
                dismissDialog()
            }
            setCancelable(cancelable)
        }.create().apply {
            if (showingDialog?.isShowing == true) {
                showingDialog?.dismiss()
            }
            if (context is LifecycleOwner) {
                context.lifecycle.addObserver(object : DefaultLifecycleObserver {
                    override fun onDestroy(owner: LifecycleOwner) {
                        this@apply.dismiss()
                        if (showingDialog === this@apply) {
                            showingDialog = null
                        }
                        super.onDestroy(owner)
                    }
                })
            }
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            showingDialog = this
            show()
        }
    }

    fun dismissDialog() {
        if (showingDialog?.isShowing == true) {
            showingDialog?.dismiss()
        }
    }
}