package com.application.channel.message

import com.application.channel.core.ChannelContextMatcher
import com.application.channel.core.SimpleDataHandler
import com.application.channel.core.client.ChannelClient
import com.application.channel.core.client.DaggerChannelClientComponent
import com.application.channel.core.handler.encoder.ByteArrayToByteBufEncoder
import com.application.channel.core.handler.encoder.ByteBufToByteArrayDecoder
import com.application.channel.core.model.ChannelContext
import com.application.channel.core.model.socketInitConfig
import com.application.channel.message.KeyModule.EncryptReleaseKey
import com.application.channel.message.database.DBProvider
import com.application.channel.message.encryptor.DecryptorDecoder
import com.application.channel.message.encryptor.EncryptorEncoder
import com.application.channel.message.meta.Message
import com.application.channel.message.meta.MessageParser
import com.application.channel.message.modules.MessageDatabaseModule
import com.application.channel.message.transformer.ByteArrayToObjectDecoder
import com.application.channel.message.transformer.JSONObjectToMessageDecoder
import com.application.channel.message.transformer.ObjectToByteArrayEncoder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author liuzhongao
 * @since 2024/5/31 00:39
 */
interface ChatService {

    val messageRepository: MessageRepository

    fun addMessageReceivedListener(listener: MessageReceiveListener)

    fun removeMessageReceivedListener(listener: MessageReceiveListener)

    fun writeMessage(message: Message, callback: Callback)

    fun startService(account: Account)

    fun stopService()
}

fun ChatService(
    initConfig: ChatServiceInitConfig
): ChatService {
    return DaggerChatServiceClientComponent
        .builder()
        .messageParserModule(MessageParserModule(initConfig.messageParserList))
        .messageDatabaseModule(MessageDatabaseModule(initConfig.messageDatabase))
        .channelClientComponent(DaggerChannelClientComponent.create())
        .build()
        .chatService()
}

internal class ChatServiceImpl @Inject constructor(
    private val channelClient: ChannelClient,
    private val listeners: MessageReceiveListenerWrapper,
    private val messageDispatcher: MessageDispatcher,
    private val authorization: Authorization,
    private val messageParser: MessageParser,
    @EncryptReleaseKey
    private val encryptKey: ByteArray,
    private val databaseProvider: DBProvider,
    override val messageRepository: MessageRepository
) : ChatService {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private var globalReconnectCount: Int = 0

    private val newValueArrivedListener = { ctx: ChannelContext, value: Any? ->
        if (value is Message) {
            this.messageDispatcher.handle(ctx, value)
        }
    }
    private val exceptionHandler = { _: ChannelContext, throwable: Throwable? ->
        throwable?.printStackTrace()
        Unit
    }
    private val onConnectionLossListener = block@{ _: ChannelContext ->
        // clear last login result message
        this.authorization.setLoginResultMessage(null)

        // schedule next reconnect and login
        if (this.globalReconnectCount >= 3) {
            return@block
        }
        val account = this.authorization.contact ?: return@block
        this.channelClient.scheduleInEventLoop(
            runnable = Runnable {
                this.startService(account)
            },
            timeout = 5000L
        )
        this.globalReconnectCount++
    }

    private val initConfig = socketInitConfig {
        address("http://127.0.0.1:8325")
        afterNewValueRead(this@ChatServiceImpl.newValueArrivedListener)
        onExceptionCreated(this@ChatServiceImpl.exceptionHandler)
        onConnectionLoss(this@ChatServiceImpl.onConnectionLossListener)
        initAdapter {
            val decoderFactories = listOf(
                ByteBufToByteArrayDecoder(),
                DecryptorDecoder(this@ChatServiceImpl.encryptKey),
                ByteArrayToObjectDecoder(),
                JSONObjectToMessageDecoder(this@ChatServiceImpl.messageParser)
            )
            val encoderFactories = listOf(
                ByteArrayToByteBufEncoder(),
                EncryptorEncoder(this@ChatServiceImpl.encryptKey),
                ObjectToByteArrayEncoder()
            )
            val handlerFactories = listOf(
                SimpleDataHandler(this@socketInitConfig.socketChannelEventListener)
            )
            decoderFactories { decoderFactories }
            encoderFactories { encoderFactories }
            handlerFactories { handlerFactories }
        }
    }

    init {
        val databaseListenerAdapter = MessagePersistenceListenerAdapter(this.databaseProvider)
        this.listeners.addListener(databaseListenerAdapter)
    }

    override fun addMessageReceivedListener(listener: MessageReceiveListener) {
        this.listeners.addListener(listener)
    }

    override fun removeMessageReceivedListener(listener: MessageReceiveListener) {
        this.listeners.removeListener(listener)
    }

    override fun writeMessage(message: Message, callback: Callback) {
        this.coroutineScope.launch {
            this@ChatServiceImpl.databaseProvider.messageDatabaseApi.persistMessage(message)
        }
        if (!this.authorization.isLogin) {
            callback.onFailure(NotAuthorizedException())
            return
        }
        this.channelClient.writeValue(
            value = MessageWritable(message),
            channelContextMatcher = ChannelContextMatcher.all,
            listener = CallbackListenerAdapter(callback)
        )
    }

    override fun startService(account: Account) {
        this.authorization.setupContact(account)
        this.channelClient.start(this.initConfig) { _ ->
            this.globalReconnectCount = 0
            val authTask = OperationAfterConnectionEstablished(
                channelClient = this.channelClient,
                account = account
            )
            this.channelClient.scheduleInEventLoop(authTask)
        }
    }

    override fun stopService() {
        this.channelClient.shutDown()
    }


}