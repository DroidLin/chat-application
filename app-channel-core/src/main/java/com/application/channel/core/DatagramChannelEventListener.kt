package com.application.channel.core

import com.application.channel.core.model.ChannelContext

/**
 * @author liuzhongao
 * @since 2024/5/27 23:32
 */
interface DatagramChannelEventListener {

    fun handleValueRead(ctx: ChannelContext, value: Any?)

    fun handleException(ctx: ChannelContext, throwable: Throwable?)
}