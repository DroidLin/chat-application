package com.application.channel.message

import com.application.channel.core.ChannelContextMatcher
import com.application.channel.core.model.ChannelContext
import com.application.channel.core.server.ChannelServer
import com.application.channel.message.meta.Message

/**
 * @author liuzhongao
 * @since 2024/6/1 21:44
 */
interface ChatServiceController {

    fun checkPeerAlive(sessionId: String): Boolean
    fun writeMessageWithSessionId(message: Message, sessionIdList: List<String>, callback: Callback)
    fun writeMessageWithContext(message: Message, channelContext: ChannelContext, callback: Callback)
    fun writeMessageWithContext(message: Message, channelContextList: List<ChannelContext>, callback: Callback)
}

internal class ChatServiceControllerImpl(
    private val channelServer: ChannelServer,
    private val authorizationMapping: AuthorizationMapping
) : ChatServiceController {

    override fun checkPeerAlive(sessionId: String): Boolean {
        val channelContextList = this.authorizationMapping.findChannelContext(sessionId)
        return channelContextList.any { channelContext -> this.channelServer.isChannelAlive(channelContext) }
    }

    override fun writeMessageWithSessionId(message: Message, sessionIdList: List<String>, callback: Callback) {
        val associatedRemotePeer = this.authorizationMapping.findChannelContext(sessionIdList)
        this.writeMessageWithContext(message, associatedRemotePeer, callback)
    }

    override fun writeMessageWithContext(message: Message, channelContext: ChannelContext, callback: Callback) {
        val channelContextList = listOf(channelContext)
        this.writeMessageWithContext(message, channelContextList, callback)
    }

    override fun writeMessageWithContext(message: Message, channelContextList: List<ChannelContext>, callback: Callback) {
        this.channelServer.write(
            value = MessageWritable(message),
            matcher = ChannelContextMatcher { channelContext ->
                channelContextList.contains(channelContext)
            },
            listener = CallbackListenerAdapter(callback)
        )
    }

}