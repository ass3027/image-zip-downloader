package com.example.image_zip_downloader

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat

class NotificationHelper(context: Context) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val appContext = context.applicationContext

    companion object {
        private const val CHANNEL_PROGRESS = "translation_progress"
        private const val CHANNEL_COMPLETE = "translation_complete"
        private const val NOTIFICATION_ID_PROGRESS = 1
        private const val NOTIFICATION_ID_COMPLETE = 2
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val progressChannel = NotificationChannel(
                CHANNEL_PROGRESS,
                "Translation Progress",
                NotificationManager.IMPORTANCE_LOW
            )
            val completeChannel = NotificationChannel(
                CHANNEL_COMPLETE,
                "Translation Complete",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannels(listOf(progressChannel, completeChannel))
        }
    }

    fun showProgress(current: Int, total: Int, folderName: String) {
        val notification = NotificationCompat.Builder(appContext, CHANNEL_PROGRESS)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Translating: $folderName ($current / $total)")
            .setProgress(total, current, false)
            .setOngoing(true)
            .setSilent(true)
            .build()
        notificationManager.notify(NOTIFICATION_ID_PROGRESS, notification)
    }

    fun showCompletion(folderName: String) {
        notificationManager.cancel(NOTIFICATION_ID_PROGRESS)
        val notification = NotificationCompat.Builder(appContext, CHANNEL_COMPLETE)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Translation Complete")
            .setContentText(folderName)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(NOTIFICATION_ID_COMPLETE, notification)
    }

    fun cancel() {
        notificationManager.cancel(NOTIFICATION_ID_PROGRESS)
    }
}
