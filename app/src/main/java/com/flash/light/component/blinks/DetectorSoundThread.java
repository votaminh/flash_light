package com.flash.light.component.blinks;

import android.content.Context;
import android.media.MediaRecorder;
import java.io.IOException;

public class DetectorSoundThread extends Thread {
    private Thread _thread;
    private int amplitudeThreshold = 6000;
    private Context context;
    private boolean isSound;
    private OnSoundListener onSoundListener;
    MediaRecorder recorder;

    public interface OnSoundListener {
        void onDetectLowAmplitudeSound();

        void onDetectSuccessSound();
    }

    public DetectorSoundThread(Context context2) {
        this.context = context2;
        init();
    }

    private void init() {
        MediaRecorder mediaRecorder = new MediaRecorder();
        this.recorder = mediaRecorder;
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        this.recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        this.recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        MediaRecorder mediaRecorder2 = this.recorder;
        mediaRecorder2.setOutputFile("/data/data/" + context.getPackageName() + "/detect.3gp");
    }

    public void start() {
        try {
            if(this.recorder == null){
                init();
            }
            this.recorder.prepare();
            this.recorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread thread = new Thread(this);
        this._thread = thread;
        thread.start();
    }

    public void stopDetection() {
        this._thread = null;
        try {
            MediaRecorder mediaRecorder = this.recorder;
            if (mediaRecorder != null) {
                mediaRecorder.stop();
                this.recorder.release();
                this.recorder = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            Thread currentThread = Thread.currentThread();
            while (this._thread == currentThread) {
                int maxAmplitude = this.recorder.getMaxAmplitude();
                Thread.sleep(500);
                boolean z = maxAmplitude >= this.amplitudeThreshold;
                this.isSound = z;
                OnSoundListener onSoundListener2 = this.onSoundListener;
                if (onSoundListener2 != null) {
                    if (z) {
                        onSoundListener2.onDetectSuccessSound();
                    } else {
                        onSoundListener2.onDetectLowAmplitudeSound();
                    }
                    this.isSound = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnSoundListener(OnSoundListener onSoundListener2) {
        this.onSoundListener = onSoundListener2;
    }
}
