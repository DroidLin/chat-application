package com.application.channel.core

import com.application.channel.core.model.ChannelContext

/**
 * @author liuzhongao
 * @since 2024/5/15 23:45
 */
interface ChannelEventListener {

    fun handleConnectionEstablished(ctx: ChannelContext) {}

    fun handleValueRead(ctx: ChannelContext, value: Any?) {}

    fun handleConnectionLoss(ctx: ChannelContext) {}

    fun handleException(ctx: ChannelContext, throwable: Throwable?) {}
}