package com.flash.light.service.audiofocus

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.AudioManager.OnAudioFocusChangeListener
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.IntDef
import androidx.annotation.RequiresApi

class AudioFocusManager(context: Context, player: AudioFocusAwarePlayer?) {
    private var audioManager = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
    private val mImpl: AudioFocusHelperImpl = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        AudioFocusHelperImplApi26(audioManager)
    } else {
        AudioFocusHelperImplBase(audioManager)
    }
    private var defaultAudioFocusChangeListener = DefaultAudioFocusListener(mImpl, player)
    private var audioFocusRequestCompat =
        AudioFocusRequestCompat.Builder(AudioManager.AUDIOFOCUS_GAIN)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            .setOnAudioFocusChangeListener(defaultAudioFocusChangeListener)
            .setWillPauseWhenDucked(true)
            .setAcceptsDelayedFocusGain(true)
            .build()

    fun requestAudioFocus(): Boolean {
        return mImpl.requestAudioFocus(audioFocusRequestCompat)
    }

    fun abandonAudioFocus() {
        mImpl.abandonAudioFocus()
    }

    fun setAudioFocusRequestCompat(audioFocusRequestCompat: AudioFocusRequestCompat) {
        this.audioFocusRequestCompat = audioFocusRequestCompat
    }

    fun getListenerForPlayer(player: AudioFocusAwarePlayer?): OnAudioFocusChangeListener {
        return DefaultAudioFocusListener(mImpl, player)
    }

    interface AudioFocusHelperImpl {
        fun requestAudioFocus(audioFocusRequestCompat: AudioFocusRequestCompat): Boolean
        fun abandonAudioFocus()
        fun willPauseWhenDucked(): Boolean
    }

    private open class AudioFocusHelperImplBase(val audioManager: AudioManager?) :
        AudioFocusHelperImpl {
        protected var audioFocusRequestCompat: AudioFocusRequestCompat? = null

        override fun requestAudioFocus(audioFocusRequestCompat: AudioFocusRequestCompat): Boolean {
            // Save the focus request.

            // Save the focus request.
            this.audioFocusRequestCompat = audioFocusRequestCompat

            // Check for possible problems...

            // Check for possible problems...
            if (audioFocusRequestCompat.acceptsDelayedFocusGain) {
                val message = "Cannot request delayed focus on API " +
                        Build.VERSION.SDK_INT

                // Make an exception to allow the developer to more easily find this code path.
                val stackTrace = UnsupportedOperationException(message)
                    .fillInStackTrace()
                Log.w("AudioFocusManager", "Cannot request delayed focus", stackTrace)
            }

            val listener = audioFocusRequestCompat.onAudioFocusChangeListener
            val streamType = audioFocusRequestCompat.audioAttributes.contentType
            val focusGain = audioFocusRequestCompat.focusGain

            return audioManager?.requestAudioFocus(listener, streamType, focusGain) ==
                    AudioManager.AUDIOFOCUS_REQUEST_GRANTED
        }

        override fun abandonAudioFocus() {
            if (audioFocusRequestCompat == null) {
                return
            }

            audioManager?.abandonAudioFocus(
                audioFocusRequestCompat?.onAudioFocusChangeListener
            )
        }

        override fun willPauseWhenDucked(): Boolean {
            if (audioFocusRequestCompat == null) {
                return false
            }

            val audioAttributes = audioFocusRequestCompat?.audioAttributes
            val pauseWhenDucked = audioFocusRequestCompat?.pauseOnDuck ?: false
            val isSpeech = audioAttributes != null &&
                    audioAttributes.contentType == AudioAttributes.CONTENT_TYPE_SPEECH
            return pauseWhenDucked || isSpeech
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private class AudioFocusHelperImplApi26(audioManager: AudioManager?) :
        AudioFocusHelperImplBase(audioManager) {
        private var audioFocusRequest: AudioFocusRequest? = null

        override fun requestAudioFocus(audioFocusRequestCompat: AudioFocusRequestCompat): Boolean {
            // Save and unwrap the compat object.

            // Save and unwrap the compat object.
            this.audioFocusRequestCompat = audioFocusRequestCompat
            val audioFocusRequest = audioFocusRequestCompat.getAudioFocusRequest()
            this.audioFocusRequest = audioFocusRequest

            return audioManager?.requestAudioFocus(audioFocusRequest) ==
                    AudioManager.AUDIOFOCUS_REQUEST_GRANTED
        }

        override fun abandonAudioFocus() {
            audioFocusRequest?.let { audioManager?.abandonAudioFocusRequest(it) }
        }
    }

    class AudioFocusRequestCompat private constructor(
        @FocusGain
        val focusGain: Int = 0,
        val onAudioFocusChangeListener: OnAudioFocusChangeListener,
        val focusChangeHandler: Handler = Handler(Looper.getMainLooper()),
        val audioAttributes: AudioAttributes,
        val pauseOnDuck: Boolean = false,
        val acceptsDelayedFocusGain: Boolean = false
    ) {
        @IntDef(
            AudioManager.AUDIOFOCUS_GAIN,
            AudioManager.AUDIOFOCUS_GAIN_TRANSIENT,
            AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK
        )
        @Retention(AnnotationRetention.SOURCE)
        annotation class FocusGain

        class Builder(@FocusGain private var focusGain: Int = 0) {

            private var onAudioFocusChangeListener: OnAudioFocusChangeListener =
                OnAudioFocusChangeListener {

                }
            private var focusChangeHandler: Handler = Handler(Looper.getMainLooper())
            private var audioAttributes: AudioAttributes =
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA).build()

            // Flags
            private var pauseOnDuck = false
            private var acceptsDelayedFocusGain = false

            constructor(audioFocusRequestCompat: AudioFocusRequestCompat) : this() {
                focusGain = audioFocusRequestCompat.focusGain
                onAudioFocusChangeListener = audioFocusRequestCompat.onAudioFocusChangeListener
                focusChangeHandler = audioFocusRequestCompat.focusChangeHandler
                audioAttributes = audioFocusRequestCompat.audioAttributes
                pauseOnDuck = audioFocusRequestCompat.pauseOnDuck
                acceptsDelayedFocusGain = audioFocusRequestCompat.acceptsDelayedFocusGain
            }

            fun setFocusGain(@FocusGain focusGain: Int): Builder {
                this.focusGain = focusGain
                return this
            }

            fun setOnAudioFocusChangeListener(listener: OnAudioFocusChangeListener): Builder {
                return setOnAudioFocusChangeListener(listener, Handler(Looper.getMainLooper()))
            }

            fun setOnAudioFocusChangeListener(
                listener: OnAudioFocusChangeListener,
                handler: Handler
            ): Builder {
                this.onAudioFocusChangeListener = listener
                this.focusChangeHandler = handler
                return this
            }

            fun setAudioAttributes(audioAttributes: AudioAttributes): Builder {
                this.audioAttributes = audioAttributes
                return this
            }

            fun setWillPauseWhenDucked(pauseOnDuck: Boolean): Builder {
                this.pauseOnDuck = pauseOnDuck
                return this
            }

            fun setAcceptsDelayedFocusGain(acceptsDelayedFocusGain: Boolean): Builder {
                this.acceptsDelayedFocusGain = acceptsDelayedFocusGain
                return this
            }

            fun build(): AudioFocusRequestCompat {
                return AudioFocusRequestCompat(
                    focusGain,
                    onAudioFocusChangeListener,
                    focusChangeHandler,
                    audioAttributes,
                    pauseOnDuck,
                    acceptsDelayedFocusGain
                )
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun getAudioFocusRequest(): AudioFocusRequest {
            return AudioFocusRequest.Builder(focusGain)
                .setAudioAttributes(audioAttributes)
                .setAcceptsDelayedFocusGain(acceptsDelayedFocusGain)
                .setWillPauseWhenDucked(pauseOnDuck)
                .setOnAudioFocusChangeListener(onAudioFocusChangeListener, focusChangeHandler)
                .build()
        }
    }

    class DefaultAudioFocusListener (private val impl: AudioFocusHelperImpl?, private val player: AudioFocusAwarePlayer?) : OnAudioFocusChangeListener {
        companion object {
            private const val MEDIA_VOLUME_DEFAULT = 1.0f
            private const val MEDIA_VOLUME_DUCK = 0.2f
        }

        private var resumeOnFocusGain = false

        override fun onAudioFocusChange(focusChange: Int) {
            when (focusChange) {
                AudioManager.AUDIOFOCUS_GAIN -> if (resumeOnFocusGain) {
                    Log.i("dienmd", "AudioFocusManager onAudioFocusChange: gain")
                    player?.play()
                    resumeOnFocusGain = false
                } else if (player?.isPlaying() == true) {
                    player.setVolume(MEDIA_VOLUME_DEFAULT)
                }

                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                    if (impl?.willPauseWhenDucked() != true) {
                        player?.setVolume(MEDIA_VOLUME_DUCK)
                    }
                    resumeOnFocusGain = player?.isPlaying() == true
                    player?.pause()
                }

                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                    resumeOnFocusGain = player?.isPlaying() == true
                    player?.pause()
                }

                AudioManager.AUDIOFOCUS_LOSS -> {
                    resumeOnFocusGain = false
                    player?.stop()
                    impl?.abandonAudioFocus()
                }
            }
        }
    }
}