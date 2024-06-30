import com.application.channel.core.SocketChannelEventListener
import com.application.channel.core.DataHandler
import com.application.channel.core.handler.encoder.ByteArrayToStringDecoder
import com.application.channel.core.handler.encoder.ByteArrayToByteBufEncoder
import com.application.channel.core.handler.encoder.StringToByteArrayEncoder
import com.application.channel.core.handler.encoder.ByteBufToByteArrayDecoder
import com.application.channel.core.model.ChannelContext
import com.application.channel.core.model.StringWritable
import com.application.channel.core.model.socketInitConfig
import com.application.channel.core.server.ChannelServer
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageEncoder

/**
 * @author liuzhongao
 * @since 2024/5/8 22:24
 */
fun main() {
    val chatServer = ChannelServer()
    val initConfig = socketInitConfig {
        address("http://localhost:8081")
        maxReconnectCount(3)
        afterNewValueRead { channelContext, any ->
            println("receive data from: ${channelContext.channelRemoteAddress}, data: $any")
            chatServer.write(StringWritable("HAHAHAHAHAH"))
        }
        initAdapter {
            encoderFactories {
                listOf(
                    ByteArrayToByteBufEncoder(),
                    StringToByteArrayEncoder(),
                    object : MessageToMessageEncoder<StringWritable>() {
                        override fun encode(ctx: ChannelHandlerContext?, msg: StringWritable?, out: MutableList<Any>?) {
                            if (ctx == null || msg == null || out == null) return
                            out += msg.value
                        }
                    }
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