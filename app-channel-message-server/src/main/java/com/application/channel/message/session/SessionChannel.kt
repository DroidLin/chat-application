package com.application.channel.message.session

import com.application.channel.message.Callback
import com.application.channel.message.meta.Message

/**
 * @author liuzhongao
 * @since 2024/5/29 23:17
 */
interface SessionChannel {

    /**
     * information about current channel, include sessionId, generate time.
     */
    val channelInfo: ChannelInfo

    val isAlive: Boolean

    /**
     * write message to current channel with success or failure callback attached.
     */
    fun writeMessage(message: Message, callback: Callback)
}