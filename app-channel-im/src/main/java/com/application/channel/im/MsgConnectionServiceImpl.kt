package com.application.channel.im

import com.application.channel.im.session.AbstractChatSession
import com.application.channel.im.session.ChatSession
import com.application.channel.im.session.ChatSessionCreationContext
import com.application.channel.im.session.ChatSessionFactory
import com.application.channel.message.*
import com.application.channel.message.meta.Message

/**
 * @author liuzhongao
 * @since 2024/6/10 11:12
 */
fun MsgConnectionService(): MsgConnectionService {
    return MsgConnectionServiceImpl()
}

private class MsgConnectionServiceImpl : MsgConnectionService {

    private val globalMessageReceiveListener = MessageReceiveListenerWrapper()

    private var initConfig: IMInitConfig? = null
    private var chatService: ChatService? = null

    override val messageRepository: MessageRepository
        get() = this.chatService?.messageRepository
            ?: throw NullPointerException("chat service not running, have you called startService yet?")

    override fun addListener(listener: MessageReceiveListener) {
        this.globalMessageReceiveListener.addListener(listener)
    }

    override fun removeListener(listener: MessageReceiveListener) {
        this.globalMessageReceiveListener.removeListener(listener)
    }

    override fun openSession(sessionId: String, sessionType: SessionType): ChatSession {
        val ctx = ChatSessionCreationContext(
            userSessionId = this.messageRepository.useSessionId,
            targetSessionId = sessionId,
            targetSessionType = sessionType
        )
        val chatSession = ChatSessionFactory.create(ctx)
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

    override fun writeMessage(message: Message, callback: Callback) {
        val chatService = this.chatService
        if (chatService == null) {
            callback.onFailure(Throwable("chat service not running"))
            return
        }
        chatService.writeMessage(message, callback)
    }

    override fun initService(initConfig: IMInitConfig): Boolean {
        if (this.initConfig == initConfig) {
            return false
        }
        this.stopService()
        val sessionId = initConfig.token.sessionId
        val availableDatabase = initConfig.factory.databaseCreate(sessionId)
        val chatServiceInitConfig = ChatServiceInitConfig(
            remoteAddress = initConfig.remoteAddress,
            messageDatabase = availableDatabase,
        )
        val chatService = ChatService(chatServiceInitConfig)
        chatService.addMessageReceivedListener(this.globalMessageReceiveListener)
        this.chatService = chatService
        this.initConfig = initConfig
        return true
    }

    override fun startService() {
        val chatService = this.chatService ?: return
        val initConfig = this.initConfig ?: return

        val sessionId = initConfig.token.sessionId
        chatService.startService(account = Account(sessionId = sessionId, accountId = sessionId))
    }

    override fun stopService() {
        this.chatService?.removeMessageReceivedListener(this.globalMessageReceiveListener)
        this.chatService?.stopService()
        this.chatService = null
    }

    override fun release() {
        this.globalMessageReceiveListener.release()
        this.initConfig = null
        this.stopService()
    }
}