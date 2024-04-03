package com.flash.light.component.light

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.viewModels
import com.flash.light.R
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
        viewBinding.run {
            btnTurnOnFlash.setOnClickListener {
                if(viewModel.isFlashTurnOn.value == false){
                    viewModel.startFlash()
                }else{
                    viewModel.stopFlash()
                }
            }

            sbAlertOnTime.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    viewModel.saveOnTimePercent(p1)
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                }

            })

            sbAlertOffTime.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    viewModel.saveOffTimePercent(p1)
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                }

            })
        }

        viewModel.getValuesSetting()
    }

    override fun initObserver() {
        viewModel.run {
            isFlashTurnOn.observe(this@FlashLightFragment){
                if(it){
                    viewBinding.run {
                        btnTurnOnFlash.setImageResource(R.drawable.ic_turn_on_flash_light)
                        layoutLock.root.visibility = View.VISIBLE
                    }

                }else{
                    viewBinding.run {
                        btnTurnOnFlash.setImageResource(R.drawable.ic_turn_off_flash_light)
                        layoutLock.root.visibility = View.INVISIBLE
                    }
                }
            }
            onTimeLive.observe(this@FlashLightFragment){
                viewBinding.secondAlertOnTime.text = it.toString() + " " + getString(R.string.txt_seconds)
            }
            offTimeLive.observe(this@FlashLightFragment){
                viewBinding.secondAlertOffTime.text = it.toString() + " " + getString(R.string.txt_seconds)
            }
            progressSbOnTimeLive.observe(this@FlashLightFragment){
                viewBinding.sbAlertOnTime.progress = it
            }
            progressSbOffTimeLive.observe(this@FlashLightFragment){
                viewBinding.sbAlertOffTime.progress = it
            }
        }
    }
}