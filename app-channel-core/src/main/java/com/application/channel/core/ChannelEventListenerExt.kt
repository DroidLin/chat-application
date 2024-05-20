package com.application.channel.core

import com.application.channel.core.model.ChannelContext


infix operator fun ChannelEventListener?.plus(other: ChannelEventListener?): ChannelEventListener? {
    return if (this != null && other != null) {
        object : ChannelEventListener {
            override fun handleValueRead(ctx: ChannelContext, value: Any?) {
                this@plus.handleValueRead(ctx, value)
                other.handleValueRead(ctx, value)
            }

            override fun handleConnectionLoss(ctx: ChannelContext) {
                this@plus.handleConnectionLoss(ctx)
                other.handleConnectionLoss(ctx)
            }

            override fun handleException(ctx: ChannelContext, throwable: Throwable?) {
                this@plus.handleException(ctx, throwable)
                other.handleException(ctx, throwable)
            }
        }
    } else this ?: other
}
