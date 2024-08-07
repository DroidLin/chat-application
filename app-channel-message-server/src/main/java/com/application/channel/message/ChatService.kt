package com.application.channel.message

import com.application.channel.core.ChannelLifecycleHandler
import com.application.channel.core.SimpleDataHandler
import com.application.channel.core.handler.encoder.ByteArrayToByteBufEncoder
import com.application.channel.core.handler.encoder.ByteBufToByteArrayDecoder
import com.application.channel.core.model.socketInitConfig
import com.application.channel.core.server.ChannelServer
import com.application.channel.core.server.DaggerChannelServerComponent
import com.application.channel.core.server.InnerChannelHolder
import com.application.channel.core.server.InnerChannelInitConfig
import com.application.channel.message.controller.Controller
import com.application.channel.message.encryptor.DecryptorDecoder
import com.application.channel.message.encryptor.EncryptorEncoder
import com.application.channel.message.meta.Message
import com.application.channel.message.meta.MessageParser
import com.application.channel.message.transformer.ByteArrayToObjectDecoder
import com.application.channel.message.transformer.JSONObjectToMessageDecoder
import com.application.channel.message.transformer.ObjectToByteArrayEncoder
import javax.inject.Inject

/**
 * @author liuzhongao
 * @since 2024/5/30 22:36
 */
interface ChatService {

    val chatServiceController: ChatServiceController

    fun startService()

    fun stopService()
}

fun ChatService(): ChatService {
    return DaggerChatServiceServerComponent
        .builder()
        .channelServerComponent(DaggerChannelServerComponent.create())
        .build()
        .chatService()
}

internal class ChatServiceImpl @Inject constructor(
    private val controller: Controller<Message>,
    private val messageParser: MessageParser,
) : ChatService {

    override val chatServiceController = ChatServiceControllerImpl(this::innerChannelHolder)

    private val initConfig = socketInitConfig {
        address("http://0.0.0.0:8081")
        afterNewValueRead { channelContext, any ->
            if (any is Message) {
                val context = Context(
                    channelContext = channelContext,
                    chatServiceController = this@ChatServiceImpl.chatServiceController
                )
                this@ChatServiceImpl.controller.handle(context, any)
            }
        }
        onExceptionCreated { _, throwable ->
            throwable?.printStackTrace()
        }
//        onConnectionLoss(this@ChatServiceImpl.authorizationMapping::removeAuthContext)
        channelAttached {}
        channelDetached {}
        initAdapter {
            val decoderFactories = listOf(
                ByteBufToByteArrayDecoder(),
                DecryptorDecoder(EncryptionKey),
                ByteArrayToObjectDecoder(),
                JSONObjectToMessageDecoder(this@ChatServiceImpl.messageParser)
            )
            val encoderFactories = listOf(
                ByteArrayToByteBufEncoder(),
                EncryptorEncoder(EncryptionKey),
                ObjectToByteArrayEncoder()
            )
            val handlerFactories = listOf(
                SimpleDataHandler(this@socketInitConfig.socketChannelEventListener),
                ChannelLifecycleHandler(this@socketInitConfig.channelLifecycleObserver)
            )
            decoderFactories { decoderFactories }
            encoderFactories { encoderFactories }
            handlerFactories { handlerFactories }
        }
    }

    private var innerChannelHolder: InnerChannelHolder? = null

    override fun startService() {
        if (this.innerChannelHolder != null) return

        val innerChannelConfig = InnerChannelInitConfig(
            initAdapter = this.initConfig.initAdapter,
            socketAddress = this.initConfig.socketAddress
        )
        this.innerChannelHolder = InnerChannelHolder(initConfig = innerChannelConfig)
        this.innerChannelHolder?.initChannelServer()
    }

    override fun stopService() {
        this.innerChannelHolder?.stopChannelServer()
        this.innerChannelHolder = null
    }
}