package com.flash.light.component.blinks

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.flash.light.R
import com.flash.light.base.fragment.BaseFragment
import com.flash.light.databinding.FragmentFlashBlinksBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FlashBlinksFragment : BaseFragment<FragmentFlashBlinksBinding>() {

    val viewModel: FlashBlinksViewModel by viewModels()

    override fun provideViewBinding(container: ViewGroup?): FragmentFlashBlinksBinding {
        return FragmentFlashBlinksBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        super.initViews()

        viewBinding.run {
            sos.setOnClickListener {
                viewModel.startSos()
            }
            dj.setOnClickListener {
                viewModel.startDJ()
            }
            btnTurnOnFlash.setOnClickListener {
                if(viewModel.stateLive.value == true){
                    viewModel.stop()
                }
            }
        }
    }

    override fun initObserver() {
        viewModel.run {
            stateLive.observe(this@FlashBlinksFragment){
                updateStateUI(it)
            }
        }
    }

    private fun updateStateUI(it: Boolean) {
        viewBinding.run {
            if(it){
                btnTurnOnFlash.animate().alpha(1f).start()
                btnTurnOnFlash.setImageResource(R.drawable.ic_turn_on_flash_light)
                layoutLock.root.visibility = View.VISIBLE
            }else{
                btnTurnOnFlash.animate().alpha(0.2f).start()
                btnTurnOnFlash.setImageResource(R.drawable.ic_turn_off_flash_light)
                layoutLock.root.visibility = View.INVISIBLE
            }
        }
    }
}