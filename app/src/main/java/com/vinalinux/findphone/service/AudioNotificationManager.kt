package com.vinalinux.findphone.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.view.ContentInfoCompat.Flags
import com.vinalinux.findphone.BuildConfig
import com.vinalinux.findphone.R
import com.vinalinux.findphone.component.main.MainActivity


class AudioNotificationManager(private val context: Context) {
    companion object {
        private const val CHANNEL_ID = "${BuildConfig.APPLICATION_ID}.audio.classifier.channel"
        private const val AUDIO_CLASSIFIER_CHANNEL = "${BuildConfig.APPLICATION_ID}.audio.classifier.detection"
        private const val REQUEST_CODE = 501
    }

    private val platformNotificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun buildNotification(): Notification {
        if (shouldCreateAudioClassifierChannel()) {
            createAudioClassifierChannel()
        }
        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(context, AUDIO_CLASSIFIER_CHANNEL)
        val description = ""

        return builder.setContentIntent(createContentIntent()) //controller.getSessionActivity()
            .setContentText(context.getString(R.string.txt_overlay_description))
            .setContentTitle(context.getString(R.string.app_name))
//            .setDeleteIntent(stopPendingIntent) //.setLargeIcon(description.iconBitmap)
//            .setOnlyAlertOnce(true)
            .setSmallIcon(R.mipmap.ic_launcher)
//            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()
    }

    private fun shouldCreateAudioClassifierChannel(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !audioClassifierChannelExists()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun audioClassifierChannelExists(): Boolean {
        return platformNotificationManager.getNotificationChannel(AUDIO_CLASSIFIER_CHANNEL) != null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createAudioClassifierChannel() {
        val notificationChannel = NotificationChannel(
            AUDIO_CLASSIFIER_CHANNEL,
            CHANNEL_ID,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationChannel.description = "AudioClassifier"
        platformNotificationManager.createNotificationChannel(notificationChannel)
    }

    private fun createContentIntent(): PendingIntent {
        val openUI = Intent(context, MainActivity::class.java)
        openUI.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        openUI.action = ServiceAction.ACTION_STOP_AUDIO_CLASSIFIER
        return PendingIntent.getActivity(
            context,
            REQUEST_CODE,
            openUI,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}