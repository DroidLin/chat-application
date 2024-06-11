import com.application.channel.message.Account
import com.application.channel.message.ChatService
import com.application.channel.message.ChatServiceInitConfig

/**
 * @author liuzhongao
 * @since 2024/6/1 20:59
 */
fun main() {
    val initConfig = ChatServiceInitConfig(remoteAddress = "http://127.0.0.1:8325", messageParserList = emptyList())
    val account = Account(sessionId = "liuzhongao", accountId = "liuzhongao")
    val chatService = ChatService(initConfig)
    chatService.startService(account)
}