package com.chat.compose.app

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.application.channel.im.MsgConnectionService
import org.koin.core.context.GlobalContext

/**
 * @author liuzhongao
 * @since 2024/6/22 11:33
 */
class AndroidChatService : Service() {

    override fun onCreate() {
        super.onCreate()
        val service: MsgConnectionService = GlobalContext.get().get()
        service.startService()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}