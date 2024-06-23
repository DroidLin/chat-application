package com.application.channel.message

import com.application.channel.core.model.ChannelContext

/**
 * @author liuzhongao
 * @since 2024/6/23 13:47
 */

open class SimpleChatServiceEventObserver : ChatServiceEventObserver {
    override fun onConnectionSuccess(channelCtx: ChannelContext) {
    }

    override fun onDatabaseInitialized() {
    }

    override fun onLogin() {
    }

    override fun onLoginFailure(event: FailureType, message: String?) {
    }

    override fun onLogout(type: LogoutReason) {
    }

    override fun onStartConnect() {
    }

    override fun onConnectionFailure(channelCtx: ChannelContext, throwable: Throwable?) {
    }

    override fun onConnectionLost(channelCtx: ChannelContext, throwable: Throwable?) {
    }
}

fun connectionSuccessObserver(function: (ChannelContext) -> Unit): ChatServiceEventObserver {
    return object : SimpleChatServiceEventObserver() {
        override fun onConnectionSuccess(channelCtx: ChannelContext) {
            function(channelCtx)
        }
    }
}