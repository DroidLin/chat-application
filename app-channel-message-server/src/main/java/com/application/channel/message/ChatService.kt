package com.application.channel.message

import com.application.channel.core.SimpleDataHandler
import com.application.channel.core.handler.encoder.ByteArrayToByteBufEncoder
import com.application.channel.core.handler.encoder.ByteBufToByteArrayDecoder
import com.application.channel.core.model.socketInitConfig
import com.application.channel.core.server.ChannelServer
import com.application.channel.core.server.DaggerChannelServerComponent
import com.application.channel.message.KeyModule.EncryptReleaseKey
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
    val channelServerComponent = DaggerChannelServerComponent.create()
    return DaggerChatServiceServerComponent
        .builder()
        .channelServerComponent(channelServerComponent)
        .build()
        .chatService()
}

internal class ChatServiceImpl @Inject constructor(
    private val channelServer: ChannelServer,
    private val authorizationMapping: AuthorizationMapping,
    private val controller: Controller<Message>,
    private val messageParser: MessageParser,
    @EncryptReleaseKey
    private val encryptKey: ByteArray
) : ChatService {

    override val chatServiceController = ChatServiceControllerImpl(this.channelServer, this.authorizationMapping)

    private val initConfig = socketInitConfig {
        address("http://127.0.0.1:8325")
        afterNewValueRead { channelContext, any ->
            if (any is Message) {
                val context = Context(
                    channelContext = channelContext,
                    chatServiceController = this@ChatServiceImpl.chatServiceController
                )
                this@ChatServiceImpl.controller.handle(context, any)
            }
        }
        onConnectionLoss(this@ChatServiceImpl.authorizationMapping::removeAuthContext)
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

    override fun startService() {
        this.channelServer.start(this.initConfig)
    }

    override fun stopService() {
        this.channelServer.shutDown()
    }
}