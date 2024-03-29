package com.flash.light.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import com.flash.light.R
import com.flash.light.databinding.DialogRateBinding

class DialogRate(context: Context) : Dialog(context) {

    private lateinit var viewBinding: DialogRateBinding
    var onRate: ((Float) -> Unit)? = null

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        setCancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DialogRateBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            when (rating) {
                0f, 1f -> {
                    viewBinding.ivStar.setImageResource(R.drawable.ic_onestar)
                }

                2f -> {
                    viewBinding.ivStar.setImageResource(R.drawable.ic_twostar)

                }

                3f -> {
                    viewBinding.ivStar.setImageResource(R.drawable.ic_threestar)

                }

                4f -> {
                    viewBinding.ivStar.setImageResource(R.drawable.ic_fourstar)

                }

                5f -> {
                    viewBinding.ivStar.setImageResource(R.drawable.ic_fivestar)
                }
            }
        }
        viewBinding.tvCancel.setOnClickListener {
            dismiss()
        }
        viewBinding.tvRate.setOnClickListener {
            dismiss()
            onRate?.invoke(viewBinding.ratingBar.rating)
        }
    }
}