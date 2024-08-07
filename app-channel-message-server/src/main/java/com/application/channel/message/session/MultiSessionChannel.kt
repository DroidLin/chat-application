package com.application.channel.message.session

/**
 * @author liuzhongao
 * @since 2024/5/30 22:34
 */
interface MultiSessionChannel : SessionChannel {

    val isAllAlive: Boolean

    /**
     * returns true while any client is alive
     */
    override val isAlive: Boolean

    fun addChannel(channel: SessionChannel)

    fun removeChannel(channel: SessionChannel)
}