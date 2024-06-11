import com.application.channel.core.client.ChannelClient
import com.application.channel.core.handler.encoder.*
import com.application.channel.core.model.DatagramWritable
import com.application.channel.core.model.channelContext
import com.application.channel.core.model.datagramInitConfig
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.DatagramPacketDecoder
import io.netty.handler.codec.DatagramPacketEncoder
import io.netty.handler.codec.MessageToMessageEncoder

/**
 * @author liuzhongao
 * @since 2024/5/28 00:04
 */

fun main() {
    val initConfig = datagramInitConfig {
        localAddress("http://0.0.0.0:9124")
        broadcast(true)
        afterNewValueRead { channelContext, any ->
            println("receive data from: ${channelContext.channelRemoteAddress}, data: ${any}")
        }
        onExceptionCreated { _, throwable ->
            throwable?.printStackTrace()
        }
        initAdapter {
            encoderFactories {
                listOf(
                    DatagramPacketEncoder(
                        object : MessageToMessageEncoder<DatagramWritable>() {
                            override fun encode(ctx: ChannelHandlerContext?, msg: DatagramWritable?, out: MutableList<Any>?) {
                                if (ctx == null || msg == null || out == null) return
                                out += msg.value.byteBuf
                            }
                        }
                    ),
                    DatagramWritableToByteArrayEncoder()
                )
            }
            decoderFactories {
                listOf(
                    DatagramPacketDecoder(ByteBufToByteArrayDecoder()),
                    ByteArrayToStringDecoder(),
                )
            }
            handlerFactories {
                listOf(
                    object : SimpleChannelInboundHandler<String>() {
                        override fun channelRead0(ctx: ChannelHandlerContext?, msg: String?) {
                            if (ctx == null || msg == null) return

                            val channelContext = ctx.channel().channelContext
                            println("receive: $msg from: ${channelContext}")

                            ctx.channel().writeAndFlush("Hello Client!")
                        }
                    }
                )
            }
        }
    }
    val chatClient = ChannelClient()
    chatClient.start(initConfig) {
        chatClient.writeValue(
            value = DatagramWritable(
                value = "Hello World!".toByteArray(),
                host = "255.255.255.255",
                port = 9123
            ),
            channelContextMatcher = { channelContext ->
                channelContext.channelRemoteAddress == initConfig.remoteSocketAddress
            },
            listener = SimpleWriteResultListener()
        )
    }
}
