package com.chat.compose.app

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android.dependencies.chat.android.R
import com.chat.compose.app.init.BACKGROUND_NOTIFICATION_CHANNEL_ID


/**
 * @author liuzhongao
 * @since 2024/6/22 11:33
 */
class AndroidChatService : Service() {

    private val notificationManagerCompat by lazy { NotificationManagerCompat.from(this) }

    override fun onCreate() {
        super.onCreate()
        val notification = NotificationCompat.Builder(this, BACKGROUND_NOTIFICATION_CHANNEL_ID)
            .setContentTitle("chat service is running.")
            .setContentText("click for more information.")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()
        startForeground(1, notification)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            notificationManagerCompat.notify(1, notification)
            return
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startChatService()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val intent = Intent(context, AndroidChatService::class.java)
            context.startService(intent)
        }

        @JvmStatic
        fun stop(context: Context) {
            val intent = Intent(context, AndroidChatService::class.java)
            context.stopService(intent)
        }
    }
}