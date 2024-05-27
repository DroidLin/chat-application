import com.application.channel.core.handler.encoder.ByteArrayToByteBufEncoder
import com.application.channel.core.handler.encoder.ByteArrayToStringDecoder
import com.application.channel.core.handler.encoder.ByteBufToByteArrayDecoder
import com.application.channel.core.handler.encoder.StringToByteArrayEncoder
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
        localAddress("http://127.0.0.1:9123")
        afterNewValueRead { channelContext, any ->
            println("receive data from: ${channelContext.channelRemoteAddress}, data: ${any}")
        }
        onExceptionCreated { _, throwable ->
            throwable?.printStackTrace()
        }
        initAdapter {
            encoderFactories(
                DatagramPacketEncoder<ByteBuf>(
                    object : MessageToMessageEncoder<ByteBuf>() {
                        override fun encode(ctx: ChannelHandlerContext?, msg: ByteBuf?, out: MutableList<Any>?) {
                            if (ctx == null || msg == null || out == null) return
                            out += msg.copy()
                        }
                    }
                ),
                object : MessageToMessageEncoder<ByteBuf>() {
                    override fun encode(ctx: ChannelHandlerContext?, msg: ByteBuf?, out: MutableList<Any>?) {
                        if (ctx == null || msg == null || out == null) return
                        out += DatagramPacket(msg.copy(), InetSocketAddress("255.255.255.255", 9124))
                    }
                },
                ByteArrayToByteBufEncoder(),
                StringToByteArrayEncoder()
            )
            decoderFactories(
                DatagramPacketDecoder(ByteBufToByteArrayDecoder()),
                ByteArrayToStringDecoder(),
            )
            handlerFactories(
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
    val chatClient = ChatServer(initConfig)
    chatClient.start()
}
