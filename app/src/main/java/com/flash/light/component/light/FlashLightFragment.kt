package com.flash.light.component.light

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.flash.light.base.fragment.BaseFragment
import com.flash.light.databinding.FragmentFlashLightBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FlashLightFragment : BaseFragment<FragmentFlashLightBinding>() {

    private val viewModel : FlashLightViewModel by viewModels()

    override fun provideViewBinding(container: ViewGroup?): FragmentFlashLightBinding {
        return FragmentFlashLightBinding.inflate(LayoutInflater.from(context))
    }

    override fun initViews() {
        viewBinding.btnTurnOnFlash.setOnClickListener {
            viewModel.startFlash()
        }
    }
}