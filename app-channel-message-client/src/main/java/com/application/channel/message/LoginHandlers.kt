package com.application.channel.message

import com.application.channel.core.model.ChannelContext
import com.application.channel.message.meta.LoginResultMessage
import com.application.channel.message.meta.LogoutMessage
import com.application.channel.message.meta.Message
import java.util.logging.Logger

/**
 * @author liuzhongao
 * @since 2024/6/23 12:19
 */
internal class LoginResultMessageHandler(
    private val authorization: Authorization,
    private val chatServiceEventObserver: ChatServiceEventObserver
) : MessageHandler {

    private val logger: Logger = Logger.getLogger("LoginResultMessageHandler")

    override fun handle(channelContext: ChannelContext, message: Message): Boolean {
        return if (message is LoginResultMessage) {
            if (message.authorized) {
                this.logger.info("authorization successfully.")
                this.authorization.setLoginResultMessage(message)
                this.chatServiceEventObserver.onLogin()
            } else this.chatServiceEventObserver.onLoginFailure(FailureType.fromValue(message.code), message.message)
            true
        } else false
    }
}

internal class LogoutMessageHandler(
    private val authorization: Authorization,
    private val chatServiceEventObserver: ChatServiceEventObserver
): MessageHandler {

    private val logger: Logger = Logger.getLogger("LoginResultMessageHandler")

    override fun handle(channelContext: ChannelContext, message: Message): Boolean {
        return if (message is LogoutMessage) {
            this.logger.info("logout, code: ${message.code}")
            this.authorization.setLoginResultMessage(null)
            this.chatServiceEventObserver.onLogout(LogoutReason.fromValue(message.code))
            true
        } else false
    }
}