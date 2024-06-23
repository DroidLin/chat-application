package com.application.channel.core.client

import com.application.channel.core.model.ChannelContext

/**
 * @author liuzhongao
 * @since 2024/6/23 03:23
 */
interface TcpConnectionObserver {

    fun onStartConnect()
    fun onConnectionSuccess(channelCtx: ChannelContext)
    fun onConnectionFailure(channelCtx: ChannelContext, throwable: Throwable?)
    fun onConnectionLost(channelCtx: ChannelContext, throwable: Throwable?)
}