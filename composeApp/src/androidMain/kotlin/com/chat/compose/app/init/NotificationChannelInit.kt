package com.chat.compose.app.init

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationManagerCompat

/**
 * @author liuzhongao
 * @since 2024/7/14 01:57
 */
const val BACKGROUND_NOTIFICATION_CHANNEL_ID = "chat.background.channel.notification"

fun initNotificationChannel(context: Context) {
    val notificationChannel =
        NotificationChannelCompat.Builder(BACKGROUND_NOTIFICATION_CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT)
            .setName("background service notification.")
            .setDescription("used for foreground service notification.")
            .build()
    NotificationManagerCompat.from(context)
        .createNotificationChannel(notificationChannel)
}