import com.application.channel.core.SocketChannelEventListener
import com.application.channel.core.DataHandler
import com.application.channel.core.handler.encoder.ByteArrayToStringDecoder
import com.application.channel.core.handler.encoder.ByteArrayToByteBufEncoder
import com.application.channel.core.handler.encoder.StringToByteArrayEncoder
import com.application.channel.core.handler.encoder.ByteBufToByteArrayDecoder
import com.application.channel.core.model.ChannelContext
import com.application.channel.core.model.socketInitConfig
import com.application.channel.core.server.ChannelServer

/**
 * @author liuzhongao
 * @since 2024/5/8 22:24
 */
fun main() {
    val initConfig = socketInitConfig {
        address("http://127.0.0.1:8325")
        maxReconnectCount(3)
        afterNewValueRead { channelContext, any ->
            println("receive data from: ${channelContext.channelRemoteAddress}, data: $any")
        }
        initAdapter {
            encoderFactories {
                listOf(
                    ByteArrayToByteBufEncoder(),
                    StringToByteArrayEncoder()
                )
            }
            decoderFactories {
                listOf(
                    ByteBufToByteArrayDecoder(),
                    ByteArrayToStringDecoder()
                )
            }
            handlerFactories {
                listOf(
                    StringDataHandler(this@socketInitConfig.socketChannelEventListener)
                )
            }
        }
    }
    val chatServer = ChannelServer()
    chatServer.start(initConfig)
}

class StringDataHandler(private val socketChannelEventListener: SocketChannelEventListener) : DataHandler<String>() {
    override fun handleConnectionEstablished(ctx: ChannelContext) {
        this.socketChannelEventListener.handleConnectionEstablished(ctx)
    }

    override fun handleValueRead(ctx: ChannelContext, value: Any?) {
        this.socketChannelEventListener.handleValueRead(ctx, value)
    }

    override fun handleConnectionLoss(ctx: ChannelContext) {
        this.socketChannelEventListener.handleConnectionLoss(ctx)
    }

    override fun handleException(ctx: ChannelContext, throwable: Throwable?) {
        this.socketChannelEventListener.handleException(ctx, throwable)
    }
}