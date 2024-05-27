import com.application.channel.core.SocketChannelEventListener
import com.application.channel.core.DataHandler
import com.application.channel.core.handler.encoder.ByteArrayToStringDecoder
import com.application.channel.core.handler.encoder.ByteArrayToByteBufEncoder
import com.application.channel.core.handler.encoder.StringToByteArrayEncoder
import com.application.channel.core.handler.encoder.ByteBufToByteArrayDecoder
import com.application.channel.core.model.ChannelContext
import com.application.channel.core.model.socketInitConfig
import com.application.channel.core.server.ChatServer

/**
 * @author liuzhongao
 * @since 2024/5/8 22:24
 */
fun main() {
    val initConfig = socketInitConfig {
        remoteAddress("http://127.0.0.1:9123")
        maxReconnectCount(3)
        afterNewValueRead { channelContext, any ->
            println("receive data from: ${channelContext.channelRemoteAddress}, data: $any")
        }
        initAdapter {
            encoderFactories(
                StringToByteArrayEncoder(),
                ByteArrayToByteBufEncoder()
            )
            decoderFactories(
                ByteBufToByteArrayDecoder(),
                ByteArrayToStringDecoder()
            )
            handlerFactories(
                StringDataHandler(this@socketInitConfig.socketChannelEventListener)
            )
        }
    }
    val chatServer = ChatServer(initConfig)
    chatServer.start()
}

class StringDataHandler(private val socketChannelEventListener: SocketChannelEventListener) : DataHandler<String>() {
    override fun handleConnectionEstablished(ctx: ChannelContext) {
        this.socketChannelEventListener.handleConnectionEstablished(ctx)
    }

    override fun handleValueRead(ctx: ChannelContext, value: String?) {
        this.socketChannelEventListener.handleValueRead(ctx, value)
    }

    override fun handleConnectionLoss(ctx: ChannelContext) {
        this.socketChannelEventListener.handleConnectionLoss(ctx)
    }

    override fun handleException(ctx: ChannelContext, throwable: Throwable?) {
        this.socketChannelEventListener.handleException(ctx, throwable)
    }
}