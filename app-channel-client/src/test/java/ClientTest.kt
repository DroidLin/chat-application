import com.application.channel.core.ChannelContextMatcher
import com.application.channel.core.client.ChannelClient
import com.application.channel.core.handler.encoder.ByteArrayToStringDecoder
import com.application.channel.core.handler.encoder.ByteArrayToByteBufEncoder
import com.application.channel.core.handler.encoder.StringToByteArrayEncoder
import com.application.channel.core.handler.encoder.ByteBufToByteArrayDecoder
import com.application.channel.core.model.ByteArrayWritable
import com.application.channel.core.model.StringWritable
import com.application.channel.core.model.socketInitConfig
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageEncoder

/**
 * @author liuzhongao
 * @since 2024/5/8 22:24
 */
fun main() {
    val initConfig = socketInitConfig {
        address("http://127.0.0.1:8325")
        maxReconnectCount(3)
        afterNewValueRead { channelContext, any ->
            println("receive data from: ${channelContext.channelRemoteAddress}, data: ${any}")
        }
        onExceptionCreated { _, throwable ->
            throwable?.printStackTrace()
        }
        initAdapter {
            encoderFactories {
                listOf(
                    ByteArrayToByteBufEncoder(),
                    StringToByteArrayEncoder(),
                    object : MessageToMessageEncoder<ByteArrayWritable>() {
                        override fun encode(ctx: ChannelHandlerContext?, msg: ByteArrayWritable?, out: MutableList<Any>?) {
                            if(ctx == null || msg == null || out == null) return
                            out += msg.value
                        }
                    },
                    object : MessageToMessageEncoder<StringWritable>() {
                        override fun encode(ctx: ChannelHandlerContext?, msg: StringWritable?, out: MutableList<Any>?) {
                            if(ctx == null || msg == null || out == null) return
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
        }
    }
    val chatClient = ChannelClient()
    chatClient.start(initConfig) {
        chatClient.writeValue(
            value = StringWritable("Hello World!"),
            channelContextMatcher = ChannelContextMatcher { channelContext ->
                channelContext.channelRemoteAddress == initConfig.socketAddress
            },
            listener = SimpleWriteResultListener()
        )
    }
//        remoteExecutor.shutDown()
}