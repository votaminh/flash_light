package com.vinalinux.findphone.component.soundsetting;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;

public class MediaPlayerContainer implements MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnInfoListener,
        MediaPlayer.OnPreparedListener, DefaultLifecycleObserver {
    public static enum MP_STATES {
        MPS_IDLE,
        MPS_INITIALIZED,
        MPS_PREPARING,
        MPS_PREPARED,
        MPS_STARTED,
        MPS_STOPPED,
        MPS_PAUSED,
        MPS_PLAYBACK_COMPLETED,
        MPS_ERROR,
        MPS_END,
    }

    private static MediaPlayerContainer s_mpm = null;
    private final MutableLiveData<MP_STATES> m_eState;
    private MediaPlayer m_mp;

    public static MediaPlayerContainer get() {
        if (null == s_mpm) {
            s_mpm = new MediaPlayerContainer();
        }
        return s_mpm;
    }

    private MediaPlayerContainer() {
        m_mp = new MediaPlayer();
        initListeners();

        m_eState = new MutableLiveData<>(MP_STATES.MPS_IDLE);
    }

    private void initListeners() {
        if (m_mp == null) return;
        m_mp.setOnBufferingUpdateListener(this);
        m_mp.setOnCompletionListener(this);
        m_mp.setOnErrorListener(this);
        m_mp.setOnInfoListener(this);
        m_mp.setOnPreparedListener(this);

        m_mp.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        release();
    }

    public MP_STATES getState() {
        return m_eState.getValue();
    }

    public boolean isPlaying() {
        return m_mp != null && m_mp.isPlaying();
    }

    public LiveData<MP_STATES> observerMediaPlayerState() {
        return m_eState;
    }

    public void setUrl(String szUrl) {
        bringToIdleState();

        try {
            m_mp.setDataSource(szUrl);
            m_eState.setValue(MP_STATES.MPS_INITIALIZED);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        m_mp.prepareAsync();
        m_eState.setValue(MP_STATES.MPS_PREPARING);
    }

    public void playRawRes(Context context, int resId, boolean isLooping) {
        try {
            release();
            m_mp = MediaPlayer.create(context, resId);
            m_mp.setAudioAttributes(new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA).build());
            initListeners();
            m_mp.setLooping(isLooping);
            m_mp.start();
            m_eState.setValue(MP_STATES.MPS_STARTED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (m_eState == null || m_eState.getValue() == null) return;
        switch (m_eState.getValue()) {
            case MPS_PREPARED, MPS_PAUSED -> {
                m_mp.start();
                m_eState.setValue(MP_STATES.MPS_STARTED);
            }
            default -> {
            }
            // !! throw exception
        }
    }

    public void pause() {
        if (m_eState == null || m_eState.getValue() == null) return;
        switch (m_eState.getValue()) {
            case MPS_STARTED -> {
                m_mp.pause();
                m_eState.setValue(MP_STATES.MPS_PAUSED);
            }
            default -> {
            }
            // !! throw exception
        }
    }

    public void release() {
        if (m_mp == null) return;
        m_mp.release();
        m_mp = null;
    }

    private void bringToIdleState() {
        if (m_mp == null) return;
        // reset() should bring MP to IDLE
        m_mp.reset();
        m_eState.setValue(MP_STATES.MPS_IDLE);
    }

    // ** Callbacks

    @Override
    public void onPrepared(MediaPlayer mp) {
        m_eState.setValue(MP_STATES.MPS_PREPARED);
        m_mp.start();
        m_eState.setValue(MP_STATES.MPS_STARTED);
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        m_eState.setValue(MP_STATES.MPS_IDLE);
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // play the next song
        m_eState.setValue(MP_STATES.MPS_END);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
    }

}
