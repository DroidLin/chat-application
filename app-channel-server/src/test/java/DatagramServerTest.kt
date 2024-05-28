import com.application.channel.core.handler.encoder.*
import com.application.channel.core.model.DatagramWritable
import com.application.channel.core.model.channelContext
import com.application.channel.core.model.datagramInitConfig
import com.application.channel.core.server.ChatServer
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.channel.socket.DatagramPacket
import io.netty.handler.codec.DatagramPacketDecoder
import io.netty.handler.codec.DatagramPacketEncoder
import io.netty.handler.codec.MessageToMessageEncoder
import java.net.InetSocketAddress

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
                arrayOf(
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
                arrayOf(
                    DatagramPacketDecoder(ByteBufToByteArrayDecoder()),
                    ByteArrayToStringDecoder(),
                )
            }
            handlerFactories {
                arrayOf(
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
    val chatClient = ChatServer(initConfig)
    chatClient.start()
}
