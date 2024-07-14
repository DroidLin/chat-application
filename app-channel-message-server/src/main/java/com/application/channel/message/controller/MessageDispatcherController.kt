package com.application.channel.message.controller

import com.application.channel.message.AuthorizationMapping
import com.application.channel.message.Callback
import com.application.channel.message.Context
import com.application.channel.message.meta.Message
import java.util.logging.Level
import java.util.logging.Logger
import javax.inject.Inject

/**
 * @author liuzhongao
 * @since 2024/6/1 21:32
 */
internal class MessageDispatcherController @Inject constructor(
    private val authorizationMapping: AuthorizationMapping,
    private val logger: Logger
) : Controller<Message> {
    override fun handle(ctx: Context, message: Message) {
        val receiverSessionId = message.receiver.sessionId
        if (receiverSessionId.isEmpty() || receiverSessionId.isBlank()) {
            return
        }

        this.logger.log(Level.INFO, "receive message: $message")
        val channelContextList = authorizationMapping.findChannelContext(receiverSessionId)
        ctx.chatServiceController.writeMessageWithContext(message, channelContextList, Callback)
    }
}