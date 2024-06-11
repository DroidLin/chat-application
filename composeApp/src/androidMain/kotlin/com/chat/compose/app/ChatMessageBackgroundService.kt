package com.chat.compose.app

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.application.channel.database.AppMessageDatabase
import com.application.channel.database.android.AppMessageDatabase
import com.application.channel.message.Account
import com.application.channel.message.ChatService
import com.application.channel.message.ChatServiceInitConfig

/**
 * @author liuzhongao
 * @since 2024/6/9 20:29
 */
class ChatMessageBackgroundService : Service() {

    private val userSessionId = "liuzhongao"
    private val chatService = ChatService(
        initConfig = ChatServiceInitConfig(
            messageDatabase = AppMessageDatabase(
                context = this,
                sessionId = this.userSessionId
            )
        )
    )

    private var startServiceCalled = false

    override fun onCreate() {
        super.onCreate()
        if (!this.startServiceCalled) {
            this.chatService.startService(Account(this.userSessionId, this.userSessionId))
            this.startServiceCalled = true
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        this.chatService.stopService()
    }
}