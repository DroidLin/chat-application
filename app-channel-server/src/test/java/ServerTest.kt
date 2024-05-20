import com.application.channel.core.ChannelEventListener
import com.application.channel.core.DataHandler
import com.application.channel.core.handler.encoder.MessageByteArrayToMessageStringDecoder
import com.application.channel.core.handler.encoder.MessageByteArrayToRawByteBufArrayEncoder
import com.application.channel.core.handler.encoder.MessageStringToMessageByteArrayEncoder
import com.application.channel.core.handler.encoder.RawByteBufArrayToMessageByteArrayDecoder
import com.application.channel.core.initAdapter
import com.application.channel.core.model.ChannelContext
import com.application.channel.core.model.SimpleSocketInitConfig
import com.application.channel.core.server.ChatServer

/**
 * @author liuzhongao
 * @since 2024/5/8 22:24
 */
fun main() {
    val channelEventListener = object : ChannelEventListener {
        override fun handleValueRead(ctx: ChannelContext, value: Any?) {
            println("receive data from: ${ctx.channelRemoteAddress}, data: ${value}")
        }
    }
    val simpleSocketInitConfigV1 = SimpleSocketInitConfig(
        remoteAddress = "http://127.0.0.1:9123",
        channelEventListener = channelEventListener,
        maxReConnectCount = 3,
        initAdapter = initAdapter {
            this.dataTransformEncoderFactory = {
                listOf(
                    MessageStringToMessageByteArrayEncoder(),
                    MessageByteArrayToRawByteBufArrayEncoder()
                )
            }
            this.dataTransformDecoderFactory = {
                listOf(
                    RawByteBufArrayToMessageByteArrayDecoder(),
                    MessageByteArrayToMessageStringDecoder()
                )
            }
            this.dataHandlerFactory = {
                listOf(
                    object : DataHandler<String>() {
                        override fun handleConnectionEstablished(ctx: ChannelContext) {
                            channelEventListener.handleConnectionEstablished(ctx)
                        }

                        override fun handleValueRead(ctx: ChannelContext, value: String?) {
                            channelEventListener.handleValueRead(ctx, value)
                        }

                        override fun handleConnectionLoss(ctx: ChannelContext) {
                            channelEventListener.handleConnectionLoss(ctx)
                        }

                        override fun handleException(ctx: ChannelContext, throwable: Throwable?) {
                            channelEventListener.handleException(ctx, throwable)
                        }
                    }
                )
            }
        }
    )
    val chatServer = ChatServer(simpleSocketInitConfigV1)
    chatServer.start()
}