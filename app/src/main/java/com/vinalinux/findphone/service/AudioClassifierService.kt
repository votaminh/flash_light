package com.vinalinux.findphone.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PixelFormat
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.camera2.CameraManager
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.AudioRecord
import android.media.MediaPlayer
import android.os.Build
import android.os.CountDownTimer
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.PowerManager
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import com.vinalinux.findphone.databinding.LayoutOverlayBinding
import com.vinalinux.findphone.domain.layer.VibrationMode
import com.vinalinux.findphone.service.audiofocus.AudioFocusAwarePlayer
import com.vinalinux.findphone.service.audiofocus.AudioFocusManager
import com.vinalinux.findphone.utils.SpManager
import com.vinalinux.findphone.utils.cancelVibrate
import com.vinalinux.findphone.utils.vibrate
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.roundToInt


@AndroidEntryPoint
class AudioClassifierService : Service(), SensorEventListener {
    private val notificationId = 404
    private val audioClassifierHandler = Handler(Looper.getMainLooper())
    private val blinkFlashHandler = Handler(Looper.getMainLooper())
    private var blinkFlashRunnable: BlinkFlashRunnable? = null
    private var notificationManager: AudioNotificationManager? = null
    private var isRunningAlertPhone = false
    private var mediaPlayer: MediaPlayer? = null
    private var countDownTimer: CountDownTimer? = null
    private var binding: LayoutOverlayBinding? = null

    private var isRunningAudioClassifier = false
    private var isForegroundService = false
    private var audioRecord: AudioRecord? = null
    private var audioFocusManager: AudioFocusManager? = null
    private var sensorManager : SensorManager? = null
    @Inject
    lateinit var spManager: SpManager
    var preBalance = 0f

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        notificationManager = AudioNotificationManager(this)
        audioFocusManager = AudioFocusManager(this, object : AudioFocusAwarePlayer {
            override fun isPlaying(): Boolean {
                return mediaPlayer?.isPlaying == true
            }

            override fun play() {
                mediaPlayer?.start()
            }

            override fun pause() {
                stopAlertPhone()
            }

            override fun stop() {
                stopAlertPhone()
            }

            override fun setVolume(volume: Float) {
                stopAlertPhone()
            }
        })
        Log.i("dienmd", "AudioClassifierService onCreate: ")
    }

    override fun onDestroy() {
        Log.i("dienmd", "AudioClassifierService onDestroy: ")
        stopAudioClassifier()
        removeWakeupView()
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (ServiceAction.ACTION_START_AUDIO_CLASSIFIER == intent?.action) {
            startClassifier()
            spManager.saveRunningAudioClassifier(true)
        } else if (ServiceAction.ACTION_STOP_AUDIO_CLASSIFIER == intent?.action) {
            stopClassifier()
            spManager.saveRunningAudioClassifier(false)
        } else if (ServiceAction.ACTION_STOP_ALERT == intent?.action) {
            stopAlertPhone()
        }
        return START_STICKY
    }

    private fun startClassifier() {
        startForeground()
        startAudioClassifier()
    }

    private fun stopClassifier() {
        stopAudioClassifier()
        stopForeground()
        stopSelf()
    }

    private fun startAudioClassifier() {
        if (isRunningAudioClassifier) {
            return
        }

        isRunningAudioClassifier = true

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        accelerometer?.let {
            sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
//        try {
//            val threshold = 0.1f + ((100 - spManager.getSensitivity()) * 0.7f / 100f)
//            val options = AudioClassifier.AudioClassifierOptions.builder()
//                .setBaseOptions(BaseOptions.builder().build())
//                .setMaxResults(1)
//                .setScoreThreshold(threshold)
//                .build()
//
//            val classifier = AudioClassifier.createFromFileAndOptions(this, "cmcm.tflite", options)
//            audioRecord = classifier.createAudioRecord()
//            val audioTensor = classifier.createInputTensorAudio()
//
//            if (audioRecord?.state == 1) {
//                audioRecord?.startRecording()
//                audioRecord?.let {
//                    val audioClassifierRunnable =
//                        AudioClassifierRunnable(audioClassifierHandler, classifier, it, audioTensor)
//                    audioClassifierRunnable.onWhistleDetected = {
//                        startAlertPhone()
//                    }
//                    audioClassifierRunnable.onClapDetected = {
//                        startAlertPhone()
//                    }
//                    audioClassifierHandler.postDelayed(audioClassifierRunnable, 100)
//                    isRunningAudioClassifier = true
//                }
//            }
//        } catch (e: UnsatisfiedLinkError) {
//            e.printStackTrace()
//        } catch (e: Exception) {
//            Log.e("exception khởi tạo : ", e.message.toString())
//        }
    }

    private fun stopAudioClassifier() {
        preBalance = 0f
        sensorManager?.unregisterListener(this)
        audioClassifierHandler.removeCallbacksAndMessages(null)
        stopAlertPhone()
        isRunningAudioClassifier = false
        audioRecord?.stop()
        audioRecord = null
    }

    private fun startForeground() {
        if (!isForegroundService) {
            notificationManager?.buildNotification()?.let {
                startForeground(notificationId, it)
                isForegroundService = true
            }
        }
    }

    private fun stopForeground() {
        if (isForegroundService) {
            stopForeground(true)
            isForegroundService = false
        }
    }

    private fun startAlertPhone() {
        if (isRunningAlertPhone) return
        isRunningAlertPhone = true
        if (spManager.getFlash()) {
            blinkFlash()
        }
        if (spManager.getVibration()) {
            vibrate()
        }
        if (spManager.getEnableVolume()) {
            adjustVolumeToMax()
            val soundModel = spManager.getSoundModel()
            playAudio(soundModel.soundRes)
        }
        if (spManager.getWakeup()) {
            wakeUp()
        }
        scheduleAlertDuration()
    }

    private fun stopAlertPhone() {
        if (!isRunningAlertPhone) return
        blinkFlashRunnable?.turnOffFlash()
        blinkFlashHandler.removeCallbacksAndMessages(null)
        blinkFlashRunnable = null
        releaseMediaPlayer()
        cancelVibrate()
        removeWakeupView()
        countDownTimer?.cancel()
        countDownTimer = null
        isRunningAlertPhone = false
    }

    private fun adjustVolumeToMax() {
        kotlin.runCatching {
            val audioManager = getSystemService(Context.AUDIO_SERVICE) as? AudioManager ?: return
            val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
            val volumeProgress = spManager.getVolume()
            val volume = maxVolume * volumeProgress / 100f
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume.roundToInt(), 0)
        }
    }

    private fun blinkFlash() {
        val isFlashAvailable = packageManager
            .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
        if (isFlashAvailable) {
            kotlin.runCatching {
                val cameraManager =
                    getSystemService(Context.CAMERA_SERVICE) as? CameraManager ?: return
                blinkFlashRunnable =
                    BlinkFlashRunnable(blinkFlashHandler, cameraManager, spManager.getFlashMode())
                blinkFlashRunnable?.let {
                    blinkFlashHandler.post(it)
                }
            }
        }
    }

    private fun playAudio(soundRes: Int) {
        if (mediaPlayer != null) {
            releaseMediaPlayer()
        }
        kotlin.runCatching {
            if (audioFocusManager?.requestAudioFocus() == true) {
                mediaPlayer = MediaPlayer.create(this, soundRes).also {
                    it.setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
                    )
                    it.setWakeMode(applicationContext, PowerManager.PARTIAL_WAKE_LOCK)
                    it.isLooping = true
                }
                mediaPlayer?.start()
            }
        }.onFailure {
        }
    }

    private fun releaseMediaPlayer() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun vibrate() {
        applicationContext.vibrate(spManager.getVibrationMode())
    }

    private fun cancelVibrate() {
        applicationContext.cancelVibrate()
    }

    private fun scheduleAlertDuration() {
        val duration = spManager.getDuration()
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(duration.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                stopAlertPhone()
            }
        }.start()
    }

    private fun wakeUp() {
        if (!Settings.canDrawOverlays(this)) return
        kotlin.runCatching {
            val powerManager = getSystemService(Context.POWER_SERVICE) as? PowerManager ?: return
            val wakeLock = powerManager.newWakeLock(
                PowerManager.FULL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
                "AppName::WakeUp"
            ) ?: return
            if (!wakeLock.isHeld) {
                val duration = spManager.getDuration()
                wakeLock.acquire(duration.toLong())
                wakeLock.release()
            }
            val windowManager = getSystemService(Context.WINDOW_SERVICE) as? WindowManager ?: return
            binding = LayoutOverlayBinding.inflate(LayoutInflater.from(this))
            binding?.ivStop?.setOnClickListener {
//                stopAlertPhone()
                stopClassifier()
            }
            val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
            binding?.let {
                windowManager.addView(it.root, params)
            }
        }.onFailure {
            it.printStackTrace()
        }
    }

    private fun removeWakeupView() {
        kotlin.runCatching {
            val windowManager = getSystemService(Context.WINDOW_SERVICE) as? WindowManager ?: return
            binding?.let {
                windowManager.removeViewImmediate(it.root)
                binding = null
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        if(isRunningAudioClassifier){
            val x = abs(event.values[0])
            val y = abs(event.values[1])
            val z = event.values[2]

            val balance = (x + y)/2f

            if(preBalance == 0f){
                preBalance = balance
            }

            // x: min -1, max 4
            // y: min -1, max 4


            val sensitivity = range(spManager.getSensitivity(), 4f, 0.1f)

            spManager.saveRunningAudioClassifier(true)

            if(abs(balance - preBalance) > sensitivity){
                startAlertPhone()
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    fun range(process: Int, start: Float, end: Float): Float {
        return (end - start) * process / 100 + start
    }
}