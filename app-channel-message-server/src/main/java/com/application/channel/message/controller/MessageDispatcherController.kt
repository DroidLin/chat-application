package com.application.channel.message.controller

import com.application.channel.message.*
import com.application.channel.message.meta.Message
import com.application.channel.message.session.channelContextList
import java.util.logging.Level
import java.util.logging.Logger
import javax.inject.Inject

/**
 * @author liuzhongao
 * @since 2024/6/1 21:32
 */
internal class MessageDispatcherController @Inject constructor(
    private val logger: Logger
) : Controller<Message> {
    override fun handle(ctx: Context, message: Message) {
        val sessionType = message.sessionType
        val receiverSessionId = message.receiver.sessionId
        if (receiverSessionId.isEmpty() || receiverSessionId.isBlank()) {
            return
        }
        this.logger.log(Level.INFO, "receive message: $message")
        val sessionChannel = when (sessionType) {
            SessionType.P2P -> SessionChannelUserPool.getSessionChannel(receiverSessionId)
            SessionType.Group -> SessionChannelChatRoomPool.getSessionChannel(receiverSessionId)
            else -> null
        } ?: return
        val channelContextList = sessionChannel.channelContextList
        ctx.chatServiceController.writeMessageWithContext(message, channelContextList, Callback)
    }
}