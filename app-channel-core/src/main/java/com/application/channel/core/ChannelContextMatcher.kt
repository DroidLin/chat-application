package com.application.channel.core

import com.application.channel.core.model.ChannelContext

/**
 * @author liuzhongao
 * @since 2024/5/18 13:13
 */
fun interface ChannelContextMatcher {

    fun match(channelContext: ChannelContext): Boolean

    companion object {
        @JvmField
        val all = ChannelContextMatcher { true }
    }
}