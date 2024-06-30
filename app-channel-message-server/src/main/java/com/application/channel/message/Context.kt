package com.application.channel.message

import com.application.channel.core.model.ChannelContext

/**
 * @author liuzhongao
 * @since 2024/6/1 21:41
 */
data class Context(
    val channelContext: ChannelContext,
    val chatServiceController: ChatServiceController,
)