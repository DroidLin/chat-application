import com.application.channel.core.ChannelContextMatcher
import com.application.channel.core.client.TcpClient
import com.application.channel.core.client.TcpConnectionObserver
import com.application.channel.core.client.TcpLocalInitConfig
import com.application.channel.core.handler.encoder.ByteArrayToByteBufEncoder
import com.application.channel.core.handler.encoder.ByteArrayToStringDecoder
import com.application.channel.core.handler.encoder.ByteBufToByteArrayDecoder
import com.application.channel.core.handler.encoder.StringToByteArrayEncoder
import com.application.channel.core.initAdapter
import com.application.channel.core.model.ByteArrayWritable
import com.application.channel.core.model.ChannelContext
import com.application.channel.core.model.StringWritable
import com.application.channel.core.model.channelContext
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.MessageToMessageEncoder
import java.net.SocketAddress

/**
 * @author liuzhongao
 * @since 2024/6/23 13:05
 */
fun main() {
    val initAdapter = initAdapter {
        encoderFactories {
            listOf(
                ByteArrayToByteBufEncoder(),
                StringToByteArrayEncoder(),
                object : MessageToMessageEncoder<ByteArrayWritable>() {
                    override fun encode(ctx: ChannelHandlerContext?, msg: ByteArrayWritable?, out: MutableList<Any>?) {
                        if (ctx == null || msg == null || out == null) return
                        out += msg.value
                    }
                },
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
                ByteArrayToStringDecoder(),
            )
        }
        handlerFactories {
            listOf(
                object : SimpleChannelInboundHandler<Any>() {
                    override fun channelRead0(ctx: ChannelHandlerContext?, msg: Any?) {
                        if (ctx == null || msg == null) return
                        println("receive data from: ${ctx.channel().channelContext.channelRemoteAddress}, data: ${msg}")
                    }
                }
            )
        }
    }

    var socketAddress: SocketAddress? = null
    var tcpClient: TcpClient? = null
    val observer = object : TcpConnectionObserver {
        override fun onStartConnect() {
        }

        override fun onConnectionSuccess(channelCtx: ChannelContext) {
            tcpClient?.write(
                writable = StringWritable("Hello World!"),
                contextMatcher = { channelContext ->
                    channelContext.channelRemoteAddress == socketAddress
                },
                listener = SimpleWriteResultListener()
            )
        }

        override fun onConnectionFailure(channelCtx: ChannelContext, throwable: Throwable?) {
        }

        override fun onConnectionLost(channelCtx: ChannelContext, throwable: Throwable?) {
        }
    }

    val tcpInitConfig = TcpLocalInitConfig(
        tcpAddress = "http://127.0.0.1:8325",
        initAdapter = initAdapter,
        observer = observer,
    )

    socketAddress = tcpInitConfig.socketAddress

    tcpClient = TcpClient()
    tcpClient.startService(tcpInitConfig)
}