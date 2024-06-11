package com.application.channel.im

import com.application.channel.im.session.ChatSession
import com.application.channel.message.MessageReceiveListener
import com.application.channel.message.MessageRepository
import com.application.channel.message.SessionType


/**
 * @author liuzhongao
 * @since 2024/6/10 11:07
 */
interface MsgConnectionService {

    val messageRepository: MessageRepository

    fun addListener(listener: MessageReceiveListener)

    fun removeListener(listener: MessageReceiveListener)

    fun openSession(sessionId: String, sessionType: SessionType): ChatSession

    fun closeSession(chatSession: ChatSession)

    fun startService(initConfig: IMInitConfig)

    fun stopService()
}