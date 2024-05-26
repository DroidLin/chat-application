import com.application.channel.core.ChannelContextMatcher
import com.application.channel.core.Listener
import com.application.channel.core.client.ChatClient
import com.application.channel.core.handler.encoder.ByteArrayToStringDecoder
import com.application.channel.core.handler.encoder.ByteArrayToByteBufEncoder
import com.application.channel.core.handler.encoder.StringToByteArrayEncoder
import com.application.channel.core.handler.encoder.ByteBufToByteArrayDecoder
import com.application.channel.core.model.socketInitConfig

/**
 * @author liuzhongao
 * @since 2024/5/8 22:24
 */
fun main() {
    val initConfig = socketInitConfig {
        remoteAddress = "http://127.0.0.1:9123"
        maxReconnectCount = 3
        afterNewValueRead { channelContext, any ->
            println("receive data from: ${channelContext.channelRemoteAddress}, data: ${any}")
        }
        onExceptionCreated { _, throwable ->
            throwable?.printStackTrace()
        }
        initAdapter {
            encoderFactories(
                ByteArrayToByteBufEncoder(),
                StringToByteArrayEncoder()
            )
            decoderFactories(
                ByteBufToByteArrayDecoder(),
                ByteArrayToStringDecoder(),
            )
        }
    }
    val chatClient = ChatClient(initConfig)
    chatClient.start {
        chatClient.writeValue(
            value = "Hello World!".toByteArray(),
            channelContextMatcher = ChannelContextMatcher { channelContext ->
                channelContext.channelRemoteAddress == initConfig.socketAddress
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