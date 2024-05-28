import com.application.channel.core.handler.encoder.ByteArrayToByteBufEncoder
import com.application.channel.core.handler.encoder.ByteArrayToStringDecoder
import com.application.channel.core.handler.encoder.ByteBufToByteArrayDecoder
import com.application.channel.core.handler.encoder.StringToByteArrayEncoder
import com.application.channel.core.model.DatagramWritable
import com.application.channel.core.model.channelContext
import io.netty.bootstrap.Bootstrap
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.DefaultAddressedEnvelope
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.DatagramChannel
import io.netty.channel.socket.DatagramPacket
import io.netty.channel.socket.nio.NioDatagramChannel
import io.netty.handler.codec.DatagramPacketDecoder
import io.netty.handler.codec.DatagramPacketEncoder
import io.netty.handler.codec.MessageToMessageEncoder
import java.net.InetSocketAddress

/**
 * @author liuzhongao
 * @since 2024/5/26 10:50
 */
fun main() {
    val eventGroup = NioEventLoopGroup()
    val channelFuture = Bootstrap()
        .group(eventGroup)
        .channel(NioDatagramChannel::class.java)
        .handler(
            object : ChannelInitializer<DatagramChannel>() {
                override fun initChannel(ch: DatagramChannel?) {
                    val pipeline = ch?.pipeline() ?: return
                    pipeline.addLast(
                        DatagramPacketEncoder<DatagramWritable>(
                            object : MessageToMessageEncoder<DatagramWritable>() {
                                override fun encode(ctx: ChannelHandlerContext?, msg: DatagramWritable?, out: MutableList<Any>?) {
                                    if (ctx == null || msg == null || out == null) return
                                    out += msg.copy()
                                }
                            }
                        )
                    )
                    pipeline.addLast(object : MessageToMessageEncoder<ByteBuf>() {
                        override fun encode(ctx: ChannelHandlerContext?, msg: ByteBuf?, out: MutableList<Any>?) {
                            if (ctx == null || msg == null || out == null) return
                            out += DatagramPacket(msg.copy(), InetSocketAddress("255.255.255.255", 8081))
                        }
                    })
                    pipeline.addLast(ByteArrayToByteBufEncoder())
                    pipeline.addLast(StringToByteArrayEncoder())

                    pipeline.addLast(DatagramPacketDecoder(ByteBufToByteArrayDecoder()))
                    pipeline.addLast(ByteArrayToStringDecoder())
                    pipeline.addLast(
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
        )
        .option(ChannelOption.SO_BROADCAST, true)
        .localAddress(8080)
        .bind().awaitUninterruptibly()
}