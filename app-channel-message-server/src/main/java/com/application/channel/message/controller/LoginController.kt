package com.application.channel.message.controller

import com.application.channel.message.Account
import com.application.channel.message.AuthorizationMapping
import com.application.channel.message.Callback
import com.application.channel.message.Context
import com.application.channel.message.meta.LoginMessage
import com.application.channel.message.meta.Messages
import java.util.logging.Logger
import javax.inject.Inject

/**
 * @author liuzhongao
 * @since 2024/6/1 12:35
 */
internal class LoginController @Inject constructor(
    private val authorizationMapping: AuthorizationMapping,
    private val logger: Logger
) : Controller<LoginMessage> {

    override fun handle(ctx: Context, message: LoginMessage) {
        val chatServiceController = ctx.chatServiceController
        val sessionId = message.sessionId
        val accountId = message.accountId

        // todo: verify sessionId and accountId
        val authorized = sessionId.isNotEmpty() && accountId.isNotEmpty()
        val loginResultMessage = Messages.buildLoginResultMessage(
            account = Account(sessionId = sessionId, accountId = accountId),
            authorized = authorized
        )
        if (authorized) {
            this.logger.info { "client: ${ctx.channelContext.channelRemoteAddress} authorized success." }
            this.authorizationMapping.putAuthContext(sessionId, ctx.channelContext)
        }
        chatServiceController.writeMessageWithContext(loginResultMessage, ctx.channelContext, Callback)
    }
}