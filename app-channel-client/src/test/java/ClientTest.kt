import com.application.channel.core.ChannelContextMatcher
import com.application.channel.core.ChannelEventListener
import com.application.channel.core.Listener
import com.application.channel.core.client.ChatClient
import com.application.channel.core.handler.encoder.MessageByteArrayToMessageStringDecoder
import com.application.channel.core.handler.encoder.MessageByteArrayToRawByteBufArrayEncoder
import com.application.channel.core.handler.encoder.MessageStringToMessageByteArrayEncoder
import com.application.channel.core.handler.encoder.RawByteBufArrayToMessageByteArrayDecoder
import com.application.channel.core.initAdapter
import com.application.channel.core.model.ChannelContext
import com.application.channel.core.model.SimpleSocketInitConfig

/**
 * @author liuzhongao
 * @since 2024/5/8 22:24
 */
fun main() {
    val channelEventListener = object : ChannelEventListener {
        override fun handleValueRead(ctx: ChannelContext, value: Any?) {
            println("receive data from: ${ctx.channelRemoteAddress}, data: ${value}")
        }

        override fun handleException(ctx: ChannelContext, throwable: Throwable?) {
            throwable?.printStackTrace()
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
                    MessageByteArrayToRawByteBufArrayEncoder(),
                )
            }
            this.dataTransformDecoderFactory = {
                listOf(
                    RawByteBufArrayToMessageByteArrayDecoder(),
                    MessageByteArrayToMessageStringDecoder(),
                )
            }
        }
    )
    val initConfig = simpleSocketInitConfigV1
    val chatClient = ChatClient(initConfig)
    chatClient.start {
        chatClient.writeValue(
            value = "Hello World!".toByteArray(),
            channelContextMatcher = ChannelContextMatcher { channelContext ->
                channelContext.channelRemoteAddress == simpleSocketInitConfigV1.socketAddress
            },
            listener = SimpleWriteResultListener()
        )
    }
//        remoteExecutor.shutDown()
}

private class SimpleWriteResultListener : Listener {
    override fun onSuccess() {
        println("send message success.")
    }

    override fun onFailure(cause: Throwable) {
        println("failure sending message.")
    }
}