package com.application.channel.core

import com.application.channel.core.model.ChannelContext

/**
 * @author liuzhongao
 * @since 2024/8/6 00:58
 */
interface ChannelLifecycleObserver {
    fun onChannelAttached(channel: ChannelContext)
    fun onChannelDetached(channel: ChannelContext)
}