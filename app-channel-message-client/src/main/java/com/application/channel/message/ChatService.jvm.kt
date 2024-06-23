package com.application.channel.message

import com.application.channel.core.ChannelContextMatcher
import com.application.channel.core.DataReader
import com.application.channel.core.SimpleListener
import com.application.channel.core.client.TcpClient
import com.application.channel.core.client.TcpLocalInitConfig
import com.application.channel.core.handler.encoder.ByteArrayToByteBufEncoder
import com.application.channel.core.handler.encoder.ByteBufToByteArrayDecoder
import com.application.channel.core.initAdapter
import com.application.channel.core.util.koinInject
import com.application.channel.message.database.DBProvider
import com.application.channel.message.encryptor.DecryptorDecoder
import com.application.channel.message.encryptor.EncryptorEncoder
import com.application.channel.message.meta.Message
import com.application.channel.message.meta.MessageParser
import com.application.channel.message.meta.Messages
import com.application.channel.message.transformer.ByteArrayToObjectDecoder
import com.application.channel.message.transformer.JSONObjectToMessageDecoder
import com.application.channel.message.transformer.ObjectToByteArrayEncoder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author liuzhongao
 * @since 2024/6/23 11:35
 */
class ChatService internal constructor(
    private val tcpClient: TcpClient,
    private val messageParser: MessageParser,
    private val authorization: Authorization
) {

    constructor() : this(koinInject(), koinInject(), koinInject())

    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val messageDispatcher = MessageDispatcher()

    private val mutableEventObserver = MutableChatServiceEventObserver()

    private var initConfig: ChatServiceInitConfig? = null
    private var databaseProvider: DBProvider? = null
    private var repository: MessageRepository? = null

    val messageRepository: MessageRepository
        get() = this.repository
            ?: this.databaseProvider?.let { dbProvider -> MessageRepository(dbProvider) }
                ?.also { repository -> this.repository = repository }
            ?: throw NullPointerException("messageRepository not initialized, have you called init database yet?")

    fun initDatabase(initConfig: ChatServiceDatabaseInitConfig) {
        val account = initConfig.account
        val database = initConfig.factory.databaseCreate(account.accountId)
        val databaseProvider = DBProvider(database, this.messageParser)
        this.databaseProvider = databaseProvider
        this.mutableEventObserver.onDatabaseInitialized()
    }

    fun startService(initConfig: ChatServiceInitConfig) {
        if (this.initConfig == initConfig) {
            return
        }
        this.initConfig = initConfig

        this.mutableEventObserver.clear()
        initConfig.chatServiceEventObserver?.also { observer ->
            this.mutableEventObserver.addObserver(observer)
        }
        this.mutableEventObserver.addObserver(
            observer = connectionSuccessObserver {
                val account = initConfig.account
                val loginMessage = Messages.buildLoginMessage(account.sessionId, account.accountId)
                this.tcpClient.write(
                    writable = MessageWritable(loginMessage),
                    listener = object : SimpleListener() {
                        override fun onFailure(cause: Throwable) {
                            this@ChatService.mutableEventObserver.onLoginFailure(FailureType.LoginConnectFailure, "")
                        }
                    }
                )
            }
        )

        this.authorization.setupContact(initConfig.account)

        val loginResultMessageHandler = LoginResultMessageHandler(
            authorization = this.authorization,
            chatServiceEventObserver = this.mutableEventObserver
        )
        val logoutMessageHandler = LogoutMessageHandler(
            authorization = this.authorization,
            chatServiceEventObserver = this.mutableEventObserver
        )
        val persistenceListenerAdapter = MessagePersistenceListenerAdapter(
            databaseProvider = this.databaseProvider,
            coroutineContext = this.coroutineScope.coroutineContext
        )

        this.messageDispatcher.clear()
        this.messageDispatcher.addHandler(loginResultMessageHandler)
        this.messageDispatcher.addHandler(logoutMessageHandler)
        this.messageDispatcher.addReceiveListener(initConfig.messageReceiveListener)
        this.messageDispatcher.addReceiveListener(persistenceListenerAdapter)

        val initAdapter = initAdapter {
            val decoderFactories = listOf(
                ByteBufToByteArrayDecoder(),
                DecryptorDecoder(initConfig.encryptionKey),
                ByteArrayToObjectDecoder(),
                JSONObjectToMessageDecoder(this@ChatService.messageParser)
            )
            val encoderFactories = listOf(
                ByteArrayToByteBufEncoder(),
                EncryptorEncoder(initConfig.encryptionKey),
                ObjectToByteArrayEncoder()
            )
            val handlerFactories = listOf(
                DataReader { ctx, data ->
                    if (data is Message) {
                        this@ChatService.messageDispatcher.handle(ctx, data)
                    }
                }
            )
            decoderFactories { decoderFactories }
            encoderFactories { encoderFactories }
            handlerFactories { handlerFactories }
        }
        val tcpInitConfig = TcpLocalInitConfig(
            tcpAddress = initConfig.tcpAddress,
            initAdapter = initAdapter,
            observer = this.mutableEventObserver
        )
        this.tcpClient.stopService()
        this.tcpClient.startService(tcpInitConfig)
        this.mutableEventObserver.onStartConnect()
    }

    fun write(message: Message, callback: Callback? = null) {
        val databaseProvider = this.databaseProvider
        if (databaseProvider != null) {
            this.coroutineScope.launch {
                databaseProvider.messageDatabaseApi.persistMessage(message)
            }
        }

        if (!this.authorization.isLogin) {
            callback?.onFailure(NotAuthorizedException())
            return
        }
        val writable = MessageWritable(message)
        val listener = if (callback != null) {
            CallbackListenerAdapter(callback)
        } else null
        this.tcpClient.write(writable, ChannelContextMatcher.all, listener)
    }

    fun stopService() {
        this.initConfig = null

        // in order to filter connection-loss events when trigger logout or stopService manually.
        this.mutableEventObserver.onLogout(LogoutReason.LogoutUserTrigger)
        this.mutableEventObserver.clear()

        this.messageDispatcher.clear()
        this.tcpClient.stopService()
    }

    fun release() {
        this.databaseProvider?.release()
        this.databaseProvider = null
        this.repository?.release()
        this.repository = null
    }
}