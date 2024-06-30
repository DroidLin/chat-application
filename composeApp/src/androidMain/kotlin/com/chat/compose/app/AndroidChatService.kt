package com.chat.compose.app

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder

/**
 * @author liuzhongao
 * @since 2024/6/22 11:33
 */
class AndroidChatService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startChatService()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? = null

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