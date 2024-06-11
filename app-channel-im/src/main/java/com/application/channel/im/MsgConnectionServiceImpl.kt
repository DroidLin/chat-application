package com.application.channel.im

import com.application.channel.im.session.AbstractChatSession
import com.application.channel.im.session.ChatSession
import com.application.channel.im.session.ChatSessionFactory
import com.application.channel.message.*

/**
 * @author liuzhongao
 * @since 2024/6/10 11:12
 */
fun MsgConnectionService(): MsgConnectionService {
    return MsgConnectionServiceImpl()
}

private class MsgConnectionServiceImpl : MsgConnectionService {

    private val globalMessageReceiveListener = MessageReceiveListenerWrapper()

    private var chatService: ChatService? = null

    override val messageRepository: MessageRepository
        get() = this.chatService?.messageRepository ?: throw NullPointerException("chat service not running, have you called startService yet?")

    override fun addListener(listener: MessageReceiveListener) {
        this.globalMessageReceiveListener.addListener(listener)
    }

    override fun removeListener(listener: MessageReceiveListener) {
        this.globalMessageReceiveListener.removeListener(listener)
    }

    override fun openSession(sessionId: String, sessionType: SessionType): ChatSession {
        val chatSession = ChatSessionFactory.create(sessionId, sessionType)
        if (chatSession is AbstractChatSession) {
            chatSession.performOnCreate()
        }
        return chatSession
    }

    override fun closeSession(chatSession: ChatSession) {
        if (chatSession is AbstractChatSession) {
            chatSession.performOnDestroy()
        }
    }

    override fun startService(initConfig: IMInitConfig) {
        this.stopService()

        val sessionId = initConfig.token.sessionId
        val availableDatabase = initConfig.factory.databaseCreate(sessionId)
        val chatServiceInitConfig = ChatServiceInitConfig(
            remoteAddress = initConfig.remoteAddress,
            messageDatabase = availableDatabase
        )
        val chatService = ChatService(chatServiceInitConfig)
        chatService.addMessageReceivedListener(this.globalMessageReceiveListener)
        chatService.startService(account = Account(sessionId = sessionId, accountId = sessionId))
        this.chatService = chatService
    }

    override fun stopService() {
        this.chatService?.removeMessageReceivedListener(this.globalMessageReceiveListener)
        this.chatService?.stopService()
        this.chatService = null
    }
}