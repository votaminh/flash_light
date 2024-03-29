package com.vinalinux.findphone.component.main

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.play.core.review.ReviewManagerFactory
import com.vinalinux.findphone.BuildConfig
import com.vinalinux.findphone.R
import com.vinalinux.findphone.admob.BaseAdmob
import com.vinalinux.findphone.admob.InterAdmob
import com.vinalinux.findphone.admob.NameRemoteAdmob
import com.vinalinux.findphone.base.activity.BaseActivity
import com.vinalinux.findphone.component.help.HelpActivity
import com.vinalinux.findphone.component.permission.PermissionActivity
import com.vinalinux.findphone.component.setting.SettingActivity
import com.vinalinux.findphone.component.sound.SoundAdapter
import com.vinalinux.findphone.component.soundsetting.MediaPlayerContainer
import com.vinalinux.findphone.component.soundsetting.SoundSettingActivity
import com.vinalinux.findphone.databinding.ActivityMainBinding
import com.vinalinux.findphone.domain.layer.SoundFlag
import com.vinalinux.findphone.domain.layer.SoundModel
import com.vinalinux.findphone.service.AudioClassifierService
import com.vinalinux.findphone.service.ServiceAction
import com.vinalinux.findphone.utils.Constant
import com.vinalinux.findphone.utils.DialogUtils.showDialogConfirmExit
import com.vinalinux.findphone.utils.NativeAdmobUtils
import com.vinalinux.findphone.utils.SpManager
import com.vinalinux.findphone.utils.Utils
import com.vinalinux.findphone.utils.isTiramisuPlus
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    private val viewModel: MainViewModel by viewModels()
    private var soundAdapter = SoundAdapter()
    @Inject
    lateinit var spManager: SpManager
    var currentPlaySound : SoundModel? = null


    private val mediaPlayerContainer = MediaPlayerContainer.get()

    private var interAdmob : InterAdmob? = null
    private var countClickItem = 0

    companion object {
        fun start(activity : Activity){
            activity.startActivity(Intent(activity, MainActivity::class.java))
        }
    }

    override fun onBack() {
        showDialogConfirmExit(submitAction = {
            finish()
        })
    }

    override fun provideViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        checkActionIntent(intent)
    }

    override fun initViews() {
        spManager.saveRunningAudioClassifier(false)

        checkActionIntent(intent)

        updateAudioClassifierProcess(spManager.isRunningAudioClassifier())
        viewBinding.ivHelp.setOnClickListener {
            startActivity(Intent(this, HelpActivity::class.java))
        }
        viewBinding.ivSetting.setOnClickListener {
            startActivity(SettingActivity.getIntent(this))
        }

        viewBinding.txtActive.setOnClickListener {
            activeAudioClassifier()
        }

        with(viewBinding){
            tv15s.setOnClickListener {
                resetColorMenuTime()
                tv15s.setTextColor(getColor(R.color.white))
                tv15s.setBackgroundResource(R.drawable.bg_main_round20)
                viewModel.saveDuration(15 * 1000)
            }
            tv30s.setOnClickListener {
                resetColorMenuTime()
                tv30s.setTextColor(getColor(R.color.white))
                tv30s.setBackgroundResource(R.drawable.bg_main_round20)
                viewModel.saveDuration(30 * 1000)
            }
            tv1m.setOnClickListener {
                resetColorMenuTime()
                tv1m.setTextColor(getColor(R.color.white))
                tv1m.setBackgroundResource(R.drawable.bg_main_round20)
                viewModel.saveDuration(60 * 1000)
            }
            tv2m.setOnClickListener {
                resetColorMenuTime()
                tv2m.setTextColor(getColor(R.color.white))
                tv2m.setBackgroundResource(R.drawable.bg_main_round20)
                viewModel.saveDuration(2 * 60 * 1000)
            }
        }

        if(spManager.getBoolean(NameRemoteAdmob.INTER_CLICK, true)){
            interAdmob = InterAdmob(this, BuildConfig.inter_click)
            interAdmob?.load(null)
        }

        buildReSound()
    }

    private fun checkActionIntent(intent: Intent) {
        val action = intent.action
        if(action == ServiceAction.ACTION_STOP_AUDIO_CLASSIFIER){
            startService(Intent(this, AudioClassifierService::class.java).also {
                it.action = ServiceAction.ACTION_STOP_AUDIO_CLASSIFIER
            })
            updateAudioClassifierProcess(false)
        }
    }

    private fun checkTimeSetting() {
        with(viewBinding){
            resetColorMenuTime()
            val duration = viewModel.getDuration()
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

    private fun resetColorMenuTime() {
        with(viewBinding){
            tv15s.setTextColor(getColor(R.color.gray5))
            tv15s.setBackgroundResource(R.color.transparent)
            tv30s.setTextColor(getColor(R.color.gray5))
            tv30s.setBackgroundResource(R.color.transparent)
            tv1m.setTextColor(getColor(R.color.gray5))
            tv1m.setBackgroundResource(R.color.transparent)
            tv2m.setTextColor(getColor(R.color.gray5))
            tv2m.setBackgroundResource(R.color.transparent)
        }
    }

    private fun buildReSound() {
        with(viewBinding.reSound){
            val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            layoutManager = linearLayoutManager
            adapter = soundAdapter
            soundAdapter.soundClick = { item, i ->
                moveToFirstSound(i)
                soundAdapter.setSelect(0)
                selectSound(item)
                this.scrollToPosition(0)
                checkCountToShowInter()
            }
            soundAdapter.playSound = { item, i ->
                playSoundNow(item, i)
            }
            soundAdapter.moreSetting = { item, i ->
                startActivity(SoundSettingActivity.getIntent(this@MainActivity, item))
            }

            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
                override fun onGlobalLayout() {
                    val latestVisible = linearLayoutManager.findLastVisibleItemPosition()
                    if(latestVisible > 0){
                        viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                    addNativeAdsSpilt(latestVisible)
                }

            })
        }
    }

    private fun addNativeAdsSpilt(latestVisible: Int) {

        if(!spManager.getBoolean(NameRemoteAdmob.NATIVE_HOME, true)){
            return
        }

        val countItemShow = latestVisible + 1
        val newList = arrayListOf<SoundModel>()
        for (i in 0 until soundAdapter.itemCount){
            if(i == 1){
                newList.add(SoundModel(0, 0,0, soundFlag = SoundFlag.NATIVE_ADS))
            }
            if(i != 0 && i % countItemShow == 0){
                newList.add(SoundModel(0, 0,0, soundFlag = SoundFlag.NATIVE_ADS))
            }
            newList.add(soundAdapter.getListData()[i])
        }
        soundAdapter.setData(newList)
    }

    private fun checkCountToShowInter() {
        countClickItem ++
        if(countClickItem % 2 == 0 && spManager.getBoolean(NameRemoteAdmob.INTER_CLICK, true)){
            interAdmob?.run {
                if(available()){
                    showInterstitial(this@MainActivity, object : BaseAdmob.OnAdmobShowListener{
                        override fun onShow() {
                            load(null)
                        }

                        override fun onError(e: String?) {
                            load(null)
                        }
                    })
                }else{
                    load(null)
                }
            }
        }
    }

    private fun playSoundNow(item: SoundModel, i: Int) {
        if (MediaPlayerContainer.MP_STATES.MPS_STARTED == mediaPlayerContainer.state) {
            mediaPlayerContainer.pause()
            if(item == currentPlaySound){
                item.isPlaying = false
                soundAdapter.notifyItemChanged(i)
            }else{
                currentPlaySound?.let {
                    it.isPlaying = false
                    val index = soundAdapter.getListData().indexOf(it)
                    soundAdapter.notifyItemChanged(index)
                }
                playSound(item)
            }
        } else {
            playSound(item)
        }
        soundAdapter.notifyItemChanged(i)
    }

    private fun moveToFirstSound(i: Int) {
        val currentList = soundAdapter.getListData()
        val sound = currentList[i]
        currentList.removeAt(i)

        val nativeSound = currentList.firstOrNull { it.soundFlag == SoundFlag.NATIVE_ADS }
        nativeSound?.let {
            currentList.remove(it)
            currentList.add(0, it)
        }

        soundAdapter.getListData().add(0, sound)

        soundAdapter.notifyDataSetChanged()
    }

    private fun playSound(item: SoundModel) {
        currentPlaySound = item
        item.isPlaying = true

        mediaPlayerContainer.playRawRes(this, item.soundRes, true)
    }

    private fun selectSound(item: SoundModel) {
        viewModel.saveSoundModel(item)
        viewBinding.imvCurrentSound.setImageResource(item.iconRes)
    }

    override fun initObserver() {
        viewModel.listData.observe(this) {
            soundAdapter.setData(it)
            updateCurrentSound()
        }

        NativeAdmobUtils.homeNativeAdmob?.apply {
            nativeAdLive.observe(this@MainActivity){
                soundAdapter.nativeAdmob = this
                soundAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun initData() {
        spManager.saveOnBoarding()
        viewModel.loadSoundModels()
    }

    override fun onResume() {
        super.onResume()
        updateCurrentSound()
        checkTimeSetting()
    }

    private fun activeAudioClassifier() {
        val isPostNotificationGranted = !isTiramisuPlus() || Utils.isPermissionGranted(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        )
        if (!Utils.isPermissionGranted(
                this,
                Manifest.permission.RECORD_AUDIO
            ) || !isPostNotificationGranted
        ) {
            startActivity(PermissionActivity.getIntent(this))
        } else {
            updateAudioClassifierProcess(!spManager.isRunningAudioClassifier())
            if (spManager.isRunningAudioClassifier()) {
                startService(Intent(this, AudioClassifierService::class.java).also {
                    it.action = ServiceAction.ACTION_STOP_AUDIO_CLASSIFIER
                })
            } else {
                ContextCompat.startForegroundService(
                    this,
                    Intent(this, AudioClassifierService::class.java).also {
                        it.action = ServiceAction.ACTION_START_AUDIO_CLASSIFIER
                    })
                showInAppReview()
            }
        }
    }
    private fun showInAppReview() {
        val isReview = spManager.getBoolean(Constant.KEY_REVIEW_APP , false)
        if (isReview) return
        val mReviewManager = ReviewManagerFactory.create(this)
        val request = mReviewManager.requestReviewFlow()
        request.addOnCompleteListener { taskInfo ->
            if (taskInfo.isSuccessful) {
                val reviewInfo = taskInfo.result
                val flow = mReviewManager.launchReviewFlow(this, reviewInfo)
                flow.addOnCompleteListener { flowTask ->
                    spManager.putBoolean(Constant.KEY_REVIEW_APP , true)
                }
            } else {
                openAppInStore()
            }
        }
    }

    private fun openAppInStore() {
        val uri =
            Uri.parse("market://details?id=" + this.packageName)
        val myAppLinkToMarket = Intent(Intent.ACTION_VIEW, uri)
        try {
            startActivity(myAppLinkToMarket)
        } catch (e: ActivityNotFoundException) {
        }
    }

    private fun updateCurrentSound() {
        val soundModel = spManager.getSoundModel()
        viewBinding.imvCurrentSound.setImageResource(soundModel.iconRes)

        selectSoundItem(soundModel)
    }

    private fun selectSoundItem(soundModel: SoundModel) {
        kotlin.runCatching {
            val index = soundAdapter.getListData().indexOfFirst { it.soundRes == soundModel.soundRes }
            moveToFirstSound(index)
            soundAdapter.setSelect(0)
        }
    }

    private fun updateAudioClassifierProcess(isRunning: Boolean) {
        viewBinding.txtActive.setText(if (isRunning) R.string.txt_tap_to_deactive else R.string.txt_tap_to_active)

        with(viewBinding){
            if(isRunning){
                maskActiveHeader.visibility = View.VISIBLE
                maskActiveTime.visibility = View.VISIBLE
                maskActiveList.visibility = View.VISIBLE
            }else{
                maskActiveHeader.visibility = View.INVISIBLE
                maskActiveTime.visibility = View.INVISIBLE
                maskActiveList.visibility = View.INVISIBLE
            }
        }

//        viewBinding.txtActive.setTextColor(
//            if (isRunning) Color.parseColor("#242424") else Color.parseColor(
//                "#BB3C0A"
//            )
//        )
    }

    override fun onPause() {
        if (MediaPlayerContainer.MP_STATES.MPS_STARTED == mediaPlayerContainer.state) {
            mediaPlayerContainer.pause()
            currentPlaySound?.let {
                it.isPlaying = false
                val index = soundAdapter.getListData().indexOf(it)
                soundAdapter.notifyItemChanged(index)
            }
        }
        super.onPause()
    }
}