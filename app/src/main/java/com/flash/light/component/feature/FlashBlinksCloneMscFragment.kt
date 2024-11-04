package com.flash.light.component.feature

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.flash.light.R
import com.flash.light.base.fragment.BaseFragment
import com.flash.light.databinding.FragmentFlashBlinksCloneMscBinding
import com.flash.light.utils.PermissionUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FlashBlinksCloneMscFragment : BaseFragment<FragmentFlashBlinksCloneMscBinding>() {

    val viewModel: FlashBlinksCloneMscViewModel by viewModels()

    override fun provideViewBinding(container: ViewGroup?): FragmentFlashBlinksCloneMscBinding {
        return FragmentFlashBlinksCloneMscBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        super.initViews()

        viewBinding.run {
            sos.setOnClickListener {
                viewModel.startSos()
            }
            dj.setOnClickListener {
                activity?.let {
                    if(PermissionUtils.isRecordAudioPermissionGranted(it)){
                        viewModel.startDJ()
                    }else{
                        PermissionUtils.requestRecordAudioPermission(it, 422)
                    }
                }
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
            stateLive.observe(this@FlashBlinksCloneMscFragment){
                updateStateUI(it)
            }
        }
    }

    private fun updateStateUI(it: Boolean) {
        viewBinding.run {
            if(it){
                btnTurnOnFlash.animate().alpha(1f).start()
                btnTurnOnFlash.setImageResource(R.drawable.ic_turn_on_flash_light_clone_msc)
                layoutLock.root.visibility = View.VISIBLE
            }else{
                btnTurnOnFlash.animate().alpha(0.2f).start()
                btnTurnOnFlash.setImageResource(R.drawable.ic_turn_off_flash_light_clone_msc)
                layoutLock.root.visibility = View.INVISIBLE
            }
        }
    }

    override fun onPause() {
        viewModel.stop()
        super.onPause()
    }
}