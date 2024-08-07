package com.application.channel.message.session

import com.application.channel.core.model.ChannelContext
import com.application.channel.core.model.unregister
import com.application.channel.message.SessionChannelUserPool
import com.application.channel.message.session.impl.ChatRoomSessionChannel
import com.application.channel.message.session.impl.MultiUserSessionChannel
import com.application.channel.message.session.impl.SingleUserSessionChannel

/**
 * @author liuzhongao
 * @since 2024/8/6 01:13
 */
internal fun SingleUserSessionChannel.autoRegistration() {
    this.channelContext.unregister {
        SessionChannelUserPool.getSessionChannel(this.channelInfo.sessionId)?.removeChannel(this)
    }
}

val SessionChannel.channelContextList: List<ChannelContext>
    get() = when (this) {
        is SingleUserSessionChannel -> listOf(this.channelContext)
        is MultiUserSessionChannel -> this.channelContextList
        is ChatRoomSessionChannel -> this.channelContextList
        else -> emptyList()
    }