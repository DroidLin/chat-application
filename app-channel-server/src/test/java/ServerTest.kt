import com.application.channel.core.ChannelEventListener
import com.application.channel.core.DataHandler
import com.application.channel.core.Factory
import com.application.channel.core.handler.encoder.ByteArrayToStringDecoder
import com.application.channel.core.handler.encoder.ByteArrayToByteBufEncoder
import com.application.channel.core.handler.encoder.StringToByteArrayEncoder
import com.application.channel.core.handler.encoder.ByteBufToByteArrayDecoder
import com.application.channel.core.initAdapter
import com.application.channel.core.model.ChannelContext
import com.application.channel.core.model.SocketChannelInitConfig
import com.application.channel.core.model.socketInitConfig
import com.application.channel.core.server.ChatServer

/**
 * @author liuzhongao
 * @since 2024/5/8 22:24
 */
fun main() {
    val initConfig = socketInitConfig {
        remoteAddress = "http://127.0.0.1:9123"
        maxReconnectCount = 3
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
                StringDataHandler(this@socketInitConfig.channelEventListener)
            )
        }
    }
    val chatServer = ChatServer(initConfig)
    chatServer.start()
}

class StringDataHandler(private val channelEventListener: ChannelEventListener) : DataHandler<String>() {
    override fun handleConnectionEstablished(ctx: ChannelContext) {
        this.channelEventListener.handleConnectionEstablished(ctx)
    }

    override fun handleValueRead(ctx: ChannelContext, value: String?) {
        this.channelEventListener.handleValueRead(ctx, value)
    }

    override fun handleConnectionLoss(ctx: ChannelContext) {
        this.channelEventListener.handleConnectionLoss(ctx)
    }

    override fun handleException(ctx: ChannelContext, throwable: Throwable?) {
        this.channelEventListener.handleException(ctx, throwable)
    }
}