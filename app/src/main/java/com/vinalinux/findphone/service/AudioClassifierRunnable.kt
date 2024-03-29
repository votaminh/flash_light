package com.vinalinux.findphone.service

import android.media.AudioRecord
import android.os.Handler
import android.util.Log
import org.tensorflow.lite.support.audio.TensorAudio
import org.tensorflow.lite.support.label.Category
import org.tensorflow.lite.task.audio.classifier.AudioClassifier
import org.tensorflow.lite.task.audio.classifier.Classifications

class AudioClassifierRunnable(
    private val handler: Handler,
    private val audioClassifier: AudioClassifier,
    private val audioRecord: AudioRecord,
    private val audioTensor: TensorAudio
) : Runnable {
    var onWhistleDetected: () -> Unit = {}
    var onClapDetected: () -> Unit = {}

    override fun run() {
        audioTensor.load(audioRecord)
        val results: List<Classifications> = audioClassifier.classify(audioTensor)
//            Log.e("test" , "Đã phát hiện tiếng vỗ tay hoặc huýt sáo")
        results.forEach {
            Log.e("result", it.toString())
        }
        if (results.firstOrNull()?.categories?.isNotEmpty() == true) {
            val listCategory = results.first().categories
            listCategory.sortByDescending { it.score }
            val category = listCategory.first()
            if (category.index != 56 && category.index != 57 && category.index != 58) {
                if (category.index == 426 || category.index == 479 || category.index == 396 || category.index == 79 || category.index == 35) {
                    Log.i(
                        "dienmd",
                        "AudioClassifierRunnable run: whistle null ${category.displayName}"
                    )
                    onWhistleDetected.invoke()
                }
            } else {
                Log.i("dienmd", "AudioClassifierRunnable run: clap null ${category.displayName}")
                onClapDetected.invoke()
            }
            handler.postDelayed(this, 100)
        } else {
            // keep detect whistle or clap
            handler.postDelayed(this, 100)
        }
    }
}