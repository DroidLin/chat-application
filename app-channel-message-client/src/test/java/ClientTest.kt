import com.application.channel.core.client.TcpClient
import com.application.channel.core.model.ChannelContext
import com.application.channel.message.*
import com.application.channel.message.meta.MessageParser

/**
 * @author liuzhongao
 * @since 2024/6/1 20:59
 */
fun main() {
    var chatService: ChatService? = null
    val initConfig = ChatServiceInitConfig(
        tcpAddress = "http://127.0.0.1:8325",
        encryptionKey = EncryptionKey,
        account = Account(sessionId = "liuzhongao", accountId = "liuzhongao"),
        messageReceiveListener = MessageReceiveListener {},
        chatServiceEventObserver = SimpleChatServiceEventObserver(),
    )
    chatService = ChatService(
        tcpClient = TcpClient(),
        messageParser = MessageParser(emptyList()),
        authorization = Authorization()
    )
    chatService.startService(initConfig)
}