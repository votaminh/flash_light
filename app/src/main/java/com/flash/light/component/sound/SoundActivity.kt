package com.flash.light.component.sound

import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.flash.light.base.activity.BaseActivity
import com.flash.light.component.main.MainAdapter
import com.flash.light.component.soundsetting.MediaPlayerContainer
import com.flash.light.component.soundsetting.SoundSettingActivity
import com.flash.light.databinding.ActivitySoundBinding
import com.flash.light.domain.layer.SoundModel
import com.flash.light.service.audiofocus.AudioFocusAwarePlayer
import com.flash.light.service.audiofocus.AudioFocusManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SoundActivity : BaseActivity<ActivitySoundBinding>() {
    override fun provideViewBinding(): ActivitySoundBinding =
        ActivitySoundBinding.inflate(layoutInflater)

    private val viewModel: SoundViewModel by viewModels()
    private var mainAdapter: MainAdapter? = null

    private val mediaPlayerContainer = MediaPlayerContainer.get()
    private var audioFocusManager: AudioFocusManager? = null

    override fun onDestroy() {
        lifecycle.removeObserver(mediaPlayerContainer)
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        mediaPlayerContainer.pause()
    }

    override fun initViews() {
        lifecycle.addObserver(mediaPlayerContainer)
        initSoundRecycle()
        viewBinding.ivBack.setOnClickListener {
            finish()
        }
        viewBinding.ivDone.setOnClickListener {
            mainAdapter?.getSoundSelectedItem()?.let { soundModel ->
                startActivity(SoundSettingActivity.getIntent(this, soundModel))
                finish()
            }
        }
        audioFocusManager = AudioFocusManager(this, object : AudioFocusAwarePlayer {
            override fun isPlaying(): Boolean {
                return mediaPlayerContainer?.isPlaying == true
            }

            override fun play() {
                mediaPlayerContainer?.play()
            }

            override fun pause() {
                mediaPlayerContainer?.pause()
            }

            override fun stop() {
                mediaPlayerContainer?.pause()
            }

            override fun setVolume(volume: Float) {
                mediaPlayerContainer?.pause()
            }
        })
    }

    override fun initObserver() {
        viewModel.listData.observe(this) {
            mainAdapter?.setData(ArrayList(it))
        }
    }

    override fun initData() {
        viewModel.loadSoundModels()
    }

    private fun initSoundRecycle() {
        val layoutManager = GridLayoutManager(this, 3)
        mainAdapter = MainAdapter(viewModel.spManager)
        viewBinding.rvSound.adapter = mainAdapter
        viewBinding.rvSound.layoutManager = layoutManager
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (mainAdapter?.isItemView(position) == true) 1 else layoutManager.spanCount
            }
        }
        mainAdapter?.onClick = {
            when (it) {
                is SoundModel -> {
                    mainAdapter?.selectItem(it)
                    viewBinding.ivDone.isVisible = true
                    if (MediaPlayerContainer.MP_STATES.MPS_STARTED == mediaPlayerContainer.state) {
                        mediaPlayerContainer.pause()
                        if (audioFocusManager?.requestAudioFocus() == true) {
                            mediaPlayerContainer.playRawRes(this, it.soundRes, false)
                        }
                    } else {
                        if (audioFocusManager?.requestAudioFocus() == true) {
                            mediaPlayerContainer.playRawRes(this, it.soundRes, false)
                        }
                    }
                }

                else -> {

                }
            }
        }
    }
}