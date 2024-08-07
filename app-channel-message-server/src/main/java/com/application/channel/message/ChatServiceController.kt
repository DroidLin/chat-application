package com.application.channel.message

import com.application.channel.core.ChannelContextMatcher
import com.application.channel.core.model.ChannelContext
import com.application.channel.core.server.ChannelServer
import com.application.channel.core.server.InnerChannelHolder
import com.application.channel.message.meta.Message

/**
 * @author liuzhongao
 * @since 2024/6/1 21:44
 */
interface ChatServiceController {
    fun writeMessageWithContext(message: Message, channelContext: ChannelContext, callback: Callback)
    fun writeMessageWithContext(message: Message, channelContextList: List<ChannelContext>, callback: Callback)
}

internal class ChatServiceControllerImpl(
    private val channelHolder: () -> InnerChannelHolder?,
) : ChatServiceController {

    override fun writeMessageWithContext(message: Message, channelContext: ChannelContext, callback: Callback) {
        val channelContextList = listOf(channelContext)
        this.writeMessageWithContext(message, channelContextList, callback)
    }

    override fun writeMessageWithContext(message: Message, channelContextList: List<ChannelContext>, callback: Callback) {
        this.channelHolder()?.writeChannel(
            contextList = channelContextList,
            writable = MessageWritable(message),
            listener = CallbackListenerAdapter(callback)
        )
    }

}