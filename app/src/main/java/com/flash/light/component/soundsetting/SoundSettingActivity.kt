package com.flash.light.component.soundsetting

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.view.forEach
import com.flash.light.BuildConfig
import com.flash.light.R
import com.flash.light.admob.BaseAdmob
import com.flash.light.admob.InterAdmob
import com.flash.light.admob.NameRemoteAdmob
import com.flash.light.admob.NativeAdmob
import com.flash.light.base.activity.BaseActivity
import com.flash.light.component.soundsetting.MediaPlayerContainer.MP_STATES
import com.flash.light.databinding.ActivitySoundSettingBinding
import com.flash.light.domain.layer.SoundModel
import com.flash.light.service.audiofocus.AudioFocusAwarePlayer
import com.flash.light.service.audiofocus.AudioFocusManager
import com.flash.light.utils.SpManager
import com.flash.light.utils.toJson
import com.flash.light.utils.toSoundModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SoundSettingActivity : BaseActivity<ActivitySoundSettingBinding>() {
    companion object {
        private const val EXTRA_SOUND = "EXTRA_SOUND"

        fun getIntent(context: Context, soundModel: SoundModel): Intent =
            Intent(context, SoundSettingActivity::class.java).also {
                it.putExtra(EXTRA_SOUND, soundModel.toJson())
            }
    }

    @Inject
    lateinit var spManager: SpManager

    private var soundSelected: SoundModel? = null
    private val viewModel: SoundSettingViewModel by viewModels()
    private val soundAdapter = SoundAdapter()
    private val mediaPlayerContainer = MediaPlayerContainer.get()
    private var audioFocusManager: AudioFocusManager? = null

    private var interAdmob : InterAdmob? = null

    override fun provideViewBinding(): ActivitySoundSettingBinding {
        return ActivitySoundSettingBinding.inflate(layoutInflater)
    }

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
        initFlash()
        initVibration()
        initVolume()
        initDuration()
        setupSelectedItem(intent?.getStringExtra(EXTRA_SOUND)?.toSoundModel() ?: SoundModel(R.drawable.img_police, R.string.txt_police, R.raw.sound_police))
        viewBinding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        viewBinding.tvApply.setOnClickListener {
            soundAdapter.selectedItem?.let {
                viewModel.saveSoundModel(it)
            }
            viewModel.saveFlash(viewBinding.swFlash.isChecked)
            viewModel.saveVibration(viewBinding.swVibration.isChecked)
            viewModel.saveEnableSound(viewBinding.swSound.isChecked)
            viewModel.saveVolume(viewBinding.seekbarVolume.progress)
            viewModel.saveDuration(getDurationSelected())

            if(interAdmob != null){
                interAdmob?.run {
                    if(available() && spManager.getBoolean(NameRemoteAdmob.INTER_APPLY, true)){
                        showInterstitial(this@SoundSettingActivity, object : BaseAdmob.OnAdmobShowListener{
                            override fun onShow() {
                                onBackPressedDispatcher.onBackPressed()
                            }
                            override fun onError(e: String?) {
                                onBackPressedDispatcher.onBackPressed()
                            }
                        })
                    }else{
                        onBackPressedDispatcher.onBackPressed()
                    }
                }
            }else{
                onBackPressedDispatcher.onBackPressed()
            }
        }
        viewBinding.ivPlay.setOnClickListener {
            if (MP_STATES.MPS_STARTED == mediaPlayerContainer.state) {
                viewBinding.imvPlay.setImageResource(R.drawable.ic_play_sound)
                mediaPlayerContainer.pause()
            } else {
                if (audioFocusManager?.requestAudioFocus() == true) {
                    viewBinding.imvPlay.setImageResource(R.drawable.ic_pause_sound)
                    soundSelected?.let { it1 -> mediaPlayerContainer.playRawRes(this, it1.soundRes, true) }
                }
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

        initInter()
        initNativeAdmob()
    }

    private fun initInter() {
        if(spManager.getBoolean(NameRemoteAdmob.INTER_APPLY, true)){
            interAdmob = InterAdmob(this, BuildConfig.inter_apply)
            interAdmob?.load(null)
        }
    }

    private fun initNativeAdmob() {
        if(spManager.getBoolean(NameRemoteAdmob.NATIVE_SOUND, true)){
            val nativeAdmob = NativeAdmob(this, BuildConfig.native_sound)
            nativeAdmob.run {
                load(object : BaseAdmob.OnAdmobLoadListener{
                    override fun onLoad() {
                        nativeAdLive.observe(this@SoundSettingActivity){
                            if(available() && spManager.getBoolean(NameRemoteAdmob.NATIVE_SOUND, true)){
                                showNative(viewBinding.flAdplaceholder, null)
                            }
                        }
                    }

                    override fun onError(e: String?) {
                        viewBinding.flAdplaceholder.visibility = View.GONE
                    }

                })
            }
        }else {
            viewBinding.flAdplaceholder.visibility = View.GONE
        }

    }

    override fun initData() {
        viewModel.loadSoundModel()
    }

    override var isReloadInter: Boolean = false

    private fun initFlash() {
        viewBinding.swFlash.isChecked = viewModel.getFlash()
    }

    private fun initVibration() {
        viewBinding.swVibration.isChecked = viewModel.getVibration()
    }

    private fun initVolume() {
        viewBinding.swSound.setOnCheckedChangeListener { _, isChecked ->
            disableView(viewBinding.seekbarVolume, isChecked, if (isChecked) 1f else 0.5f)
        }

        if(!viewModel.getEnableVolume()){
            disableView(viewBinding.seekbarVolume, false, 0.5f)
        }

        viewBinding.swSound.isChecked = viewModel.getEnableVolume()
        viewBinding.seekbarVolume.setProgress(viewModel.getVolume())
    }

    private fun initDuration() {
        viewBinding.tv15s.setOnClickListener {
            selectDuration(viewBinding.tv15s)
        }
        viewBinding.tv30s.setOnClickListener {
            selectDuration(viewBinding.tv30s)
        }
        viewBinding.tv1m.setOnClickListener {
            selectDuration(viewBinding.tv1m)
        }
        viewBinding.tv2m.setOnClickListener {
            selectDuration(viewBinding.tv2m)
        }

        val duration = viewModel.getDuration()
        viewBinding.tv15s.isSelected = duration == 15 * 1000
        viewBinding.tv30s.isSelected = duration == 30 * 1000
        viewBinding.tv1m.isSelected = duration == 60 * 1000
        viewBinding.tv2m.isSelected = duration == 2 * 60 * 1000

        with(viewBinding){
            if(duration == 15 * 1000){
                tv15s.setTextColor(getColor(R.color.white))
                tv15s.setBackgroundResource(R.drawable.bg_main_round20)
            }else if(duration == 30 * 1000){
                tv30s.setTextColor(getColor(R.color.white))
                tv30s.setBackgroundResource(R.drawable.bg_main_round20)
            }else if(duration == 60 * 1000){
                tv1m.setTextColor(getColor(R.color.white))
                tv1m.setBackgroundResource(R.drawable.bg_main_round20)
            }else if(duration == 2 * 60 * 1000){
                tv2m.setTextColor(getColor(R.color.white))
                tv2m.setBackgroundResource(R.drawable.bg_main_round20)
            }
        }
    }

    private fun selectDuration(tv: TextView) {
        with(viewBinding){
            tv15s.setTextColor(getColor(R.color.gray5))
            tv15s.setBackgroundResource(R.color.transparent)
            tv30s.setTextColor(getColor(R.color.gray5))
            tv30s.setBackgroundResource(R.color.transparent)
            tv1m.setTextColor(getColor(R.color.gray5))
            tv1m.setBackgroundResource(R.color.transparent)
            tv2m.setTextColor(getColor(R.color.gray5))
            tv2m.setBackgroundResource(R.color.transparent)

            tv.setTextColor(getColor(R.color.white))
            tv.setBackgroundResource(R.drawable.bg_main_round20)
        }
        viewBinding.tv15s.isSelected = false
        viewBinding.tv30s.isSelected = false
        viewBinding.tv1m.isSelected = false
        viewBinding.tv2m.isSelected = false
        tv.isSelected = true
    }

    private fun setupSelectedItem(soundModel: SoundModel) {
        soundSelected = soundModel
        viewBinding.tvTitle.setText(soundModel.nameRes)
        viewBinding.imvCurrentSound.setImageResource(soundModel.iconRes)
    }

    private fun getDurationSelected(): Int {
        return if (viewBinding.tv15s.isSelected) {
            15 * 1000
        } else if (viewBinding.tv30s.isSelected) {
            30 * 1000
        } else if (viewBinding.tv1m.isSelected) {
            60 * 1000
        } else if (viewBinding.tv2m.isSelected) {
            2 * 60 * 1000
        } else {
            15 * 1000
        }
    }

    private fun disableView(view: View, disable: Boolean, alpha: Float) {
        view.isEnabled = disable
        view.alpha = alpha
        if (view is ViewGroup) {
            view.forEach {
                disableView(it, disable, alpha)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewBinding.imvPlay.setImageResource(R.drawable.ic_play_sound)
    }
}