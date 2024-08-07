package com.application.channel.message.controller

import com.application.channel.message.*
import com.application.channel.message.meta.LoginMessage
import com.application.channel.message.meta.Messages
import com.application.channel.message.session.autoRegistration
import java.util.logging.Logger
import javax.inject.Inject

/**
 * @author liuzhongao
 * @since 2024/6/1 12:35
 */
internal class LoginController @Inject constructor(
    private val logger: Logger
) : Controller<LoginMessage> {

    override fun handle(ctx: Context, message: LoginMessage) {
        val chatServiceController = ctx.chatServiceController
        val sessionId = message.sessionId
        val accountId = message.accountId

        // todo: verify sessionId and accountId
        val authorized = sessionId.isNotEmpty() && accountId.isNotEmpty()
        val loginResultMessage = Messages.buildLoginResultMessage(
            account = Account(
                sessionId = sessionId,
                accountId = accountId
            ),
            authorized = authorized
        )
        if (authorized) {
            this.logger.info { "client: ${ctx.channelContext.channelRemoteAddress} authorized success." }
            val singleSessionChannel = SessionChannelUserPool.newSingleSessionChannel(sessionId, ctx.channelContext)
            singleSessionChannel.autoRegistration()
            SessionChannelUserPool.getSessionChannelOrCreate(sessionId).addChannel(singleSessionChannel)
        }
        chatServiceController.writeMessageWithContext(loginResultMessage, ctx.channelContext, Callback)
    }
}