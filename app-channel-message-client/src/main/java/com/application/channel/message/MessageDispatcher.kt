package com.application.channel.message

import com.application.channel.core.model.ChannelContext
import com.application.channel.message.meta.LoginResultMessage
import com.application.channel.message.meta.Message
import java.util.logging.Logger
import javax.inject.Inject

/**
 * @author liuzhongao
 * @since 2024/6/2 10:31
 */
internal class MessageDispatcher @Inject constructor(
    private val loginResultMessageHandler: LoginResultMessageHandler,
    private val listeners: MessageReceiveListenerWrapper,
) : MessageHandler {
    override fun handle(channelContext: ChannelContext, message: Message): Boolean {
        if (!this.loginResultMessageHandler.handle(channelContext, message)) {
            this.listeners.onReceive(message)
        }
        return true
    }
}

internal class LoginResultMessageHandler @Inject constructor(
    private val authorization: Authorization,
    private val logger: Logger
): MessageHandler {
    override fun handle(channelContext: ChannelContext, message: Message): Boolean {
        return if (message is LoginResultMessage) {
            if (message.authorized) {
                this.logger.info { "authorization successfully." }
                this.authorization.setLoginResultMessage(message)
            }
            true
        } else false
    }
}