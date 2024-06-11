import com.application.channel.core.handler.encoder.*
import com.application.channel.core.model.DatagramWritable
import com.application.channel.core.model.channelContext
import com.application.channel.core.model.datagramInitConfig
import com.application.channel.core.server.ChannelServer
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.DatagramPacketDecoder
import io.netty.handler.codec.DatagramPacketEncoder
import io.netty.handler.codec.MessageToMessageEncoder

/**
 * @author liuzhongao
 * @since 2024/5/28 00:36
 */
fun main() {
    val initConfig = datagramInitConfig {
        localAddress("http://0.0.0.0:9123")
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

                            ctx.channel().writeAndFlush(DatagramWritable("Hello Client!".toByteArray(), "255.255.255.255", 9124))
                        }
                    }
                )
            }
        }
    }
    val chatClient = ChannelServer()
    chatClient.start(initConfig)
}
