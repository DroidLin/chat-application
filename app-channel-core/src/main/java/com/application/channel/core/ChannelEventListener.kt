package com.application.channel.core

import com.application.channel.core.model.ChannelContext
import com.application.channel.core.model.MultiInitConfig

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

fun ChannelEventListener(raw: ChannelEventListener?): ChannelEventListener {
    return ChannelEventListenerWrapper(raw)
}

private class ChannelEventListenerWrapper(private val raw: ChannelEventListener?) : ChannelEventListener {
    override fun handleConnectionEstablished(ctx: ChannelContext) {
        this.raw?.handleConnectionLoss(ctx)
    }

    override fun handleValueRead(ctx: ChannelContext, value: Any?) {
        this.raw?.handleValueRead(ctx, value)
    }

    override fun handleConnectionLoss(ctx: ChannelContext) {
        this.raw?.handleConnectionLoss(ctx)
    }

    override fun handleException(ctx: ChannelContext, throwable: Throwable?) {
        this.raw?.handleException(ctx, throwable)
    }
}

fun ChannelEventListener(multiInitConfig: MultiInitConfig): ChannelEventListener {
    return KeyedChannelEventListener(multiInitConfig)
}

private class KeyedChannelEventListener(private val multiInitConfig: MultiInitConfig): ChannelEventListener {
    override fun handleConnectionEstablished(ctx: ChannelContext) {
        this.notifyEventListener(ctx) { this.handleConnectionEstablished(ctx) }
    }

    override fun handleValueRead(ctx: ChannelContext, value: Any?) {
        this.notifyEventListener(ctx) { this.handleValueRead(ctx, value) }
    }

    override fun handleConnectionLoss(ctx: ChannelContext) {
        this.notifyEventListener(ctx) { this.handleConnectionLoss(ctx) }
    }

    override fun handleException(ctx: ChannelContext, throwable: Throwable?) {
        this.notifyEventListener(ctx) { this.handleException(ctx, throwable) }
    }

    private inline fun notifyEventListener(ctx: ChannelContext, function: ChannelEventListener.() -> Unit) {
        this.multiInitConfig.initConfigList.find { ctx.channelLocalAddress == it.socketAddress }
            ?.channelEventListener?.apply(function)
    }
}