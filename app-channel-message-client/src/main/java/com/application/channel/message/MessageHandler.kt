package com.application.channel.message

import com.application.channel.core.model.ChannelContext
import com.application.channel.message.meta.Message

/**
 * @author liuzhongao
 * @since 2024/6/2 10:29
 */
interface MessageHandler {

    /**
     * handle [message] with customized logics, returns true to indicate
     * this handler handle the message and will not be dispatched further
     */
    fun handle(channelContext: ChannelContext, message: Message): Boolean
}