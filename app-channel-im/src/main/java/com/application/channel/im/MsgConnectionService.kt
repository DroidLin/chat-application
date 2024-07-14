package com.application.channel.im

import com.application.channel.im.session.AbstractChatSession
import com.application.channel.im.session.ChatSession
import com.application.channel.im.session.ChatSessionCreationContext
import com.application.channel.im.session.ChatSessionFactory
import com.application.channel.message.*
import com.application.channel.message.meta.Message


/**
 * @author liuzhongao
 * @since 2024/6/10 11:07
 */
interface MsgConnectionService {

    val messageRepository: MessageRepository

    fun addListener(listener: MessageReceiveListener)
    fun removeListener(listener: MessageReceiveListener)

    fun addEventObserver(chatServiceEventObserver: ChatServiceEventObserver)
    fun removeEventObserver(chatServiceEventObserver: ChatServiceEventObserver)

    fun openSession(sessionId: String, sessionType: SessionType): ChatSession
    fun closeSession(chatSession: ChatSession)

    fun writeMessage(message: Message, callback: Callback)

    fun initDatabase(initConfig: IMDatabaseInitConfig): Boolean
    fun startService(initConfig: IMInitConfig)
    fun startService(initConfig: IMInitConfig, force: Boolean)
    fun stopService()
    fun release()
}

fun MsgConnectionService(): MsgConnectionService {
    return MsgConnectionServiceImpl()
}

private class MsgConnectionServiceImpl : MsgConnectionService {

    private val globalMessageReceiveListener = MessageReceiveListenerWrapper()
    private val chatService: ChatService = ChatService()

    private var databaseInitConfig: IMDatabaseInitConfig? = null
    private var initConfig: IMInitConfig? = null

    override val messageRepository: MessageRepository
        get() = this.chatService.messageRepository

    private val mutableEventObserver = MutableChatServiceEventObserver()

    override fun addListener(listener: MessageReceiveListener) {
        this.globalMessageReceiveListener.addListener(listener)
    }

    override fun removeListener(listener: MessageReceiveListener) {
        this.globalMessageReceiveListener.removeListener(listener)
    }

    override fun addEventObserver(chatServiceEventObserver: ChatServiceEventObserver) {
        this.mutableEventObserver.addObserver(chatServiceEventObserver)
    }

    override fun removeEventObserver(chatServiceEventObserver: ChatServiceEventObserver) {
        this.mutableEventObserver.removeObserver(chatServiceEventObserver)
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
        this.chatService.write(message, callback)
    }

    override fun initDatabase(initConfig: IMDatabaseInitConfig): Boolean {
        if (this.databaseInitConfig == initConfig) {
            return false
        }
        this.databaseInitConfig = initConfig
        this.chatService.initDatabase(
            initConfig = ChatServiceDatabaseInitConfig(
                account = initConfig.account,
                factory = initConfig.factory
            )
        )
        return true
    }

    override fun startService(initConfig: IMInitConfig) {
        this.startService(initConfig, false)
    }

    override fun startService(initConfig: IMInitConfig, force: Boolean) {
        if (this.initConfig == initConfig && !force) {
            return
        }
        this.initConfig = initConfig

        this.chatService.startService(
            initConfig = ChatServiceInitConfig(
                tcpAddress = initConfig.remoteAddress,
                encryptionKey = EncryptionKey,
                account = initConfig.account,
                messageReceiveListener = this.globalMessageReceiveListener,
                chatServiceEventObserver = this.mutableEventObserver
            ),
            force = force
        )
    }

    override fun stopService() {
        this.chatService.stopService()
    }

    override fun release() {
        this.globalMessageReceiveListener.clear()
        this.initConfig = null
        this.stopService()
        this.chatService.release()
    }
}