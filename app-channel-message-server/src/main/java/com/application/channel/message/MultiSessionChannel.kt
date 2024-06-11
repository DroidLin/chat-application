package com.application.channel.message

import com.application.channel.message.session.SessionChannel

/**
 * @author liuzhongao
 * @since 2024/5/30 22:34
 */
interface MultiSessionChannel : SessionChannel {

    val anyPointAlive: Boolean

    fun addChannel(channel: SessionChannel)

    fun removeChannel(channel: SessionChannel)
}