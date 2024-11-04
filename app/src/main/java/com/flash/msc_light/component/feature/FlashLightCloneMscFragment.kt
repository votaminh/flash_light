package com.flash.msc_light.component.feature

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.viewModels
import com.flash.msc_light.R
import com.flash.msc_light.base.fragment.BaseFragment
import com.flash.msc_light.databinding.FragmentFlashLightCloneMscBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FlashLightCloneMscFragment : BaseFragment<FragmentFlashLightCloneMscBinding>() {

    private val viewModel : FlashLightCloneMscViewModel by viewModels()

    override fun provideViewBinding(container: ViewGroup?): FragmentFlashLightCloneMscBinding {
        return FragmentFlashLightCloneMscBinding.inflate(LayoutInflater.from(context))
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
            isFlashTurnOn.observe(this@FlashLightCloneMscFragment){
                if(it){
                    viewBinding.run {
                        btnTurnOnFlash.setImageResource(R.drawable.ic_turn_on_flash_light_clone_msc)
                        layoutLock.root.visibility = View.VISIBLE
                    }

                }else{
                    viewBinding.run {
                        btnTurnOnFlash.setImageResource(R.drawable.ic_turn_off_flash_light_clone_msc)
                        layoutLock.root.visibility = View.INVISIBLE
                    }
                }
            }
            onTimeLive.observe(this@FlashLightCloneMscFragment){
                viewBinding.secondAlertOnTime.text = it.toString() + " " + getString(R.string.txt_seconds)
            }
            offTimeLive.observe(this@FlashLightCloneMscFragment){
                viewBinding.secondAlertOffTime.text = it.toString() + " " + getString(R.string.txt_seconds)
            }
            progressSbOnTimeLive.observe(this@FlashLightCloneMscFragment){
                viewBinding.sbAlertOnTime.progress = it
            }
            progressSbOffTimeLive.observe(this@FlashLightCloneMscFragment){
                viewBinding.sbAlertOffTime.progress = it
            }
        }
    }

    override fun onPause() {
        viewModel.stopFlash()
        super.onPause()
    }
}