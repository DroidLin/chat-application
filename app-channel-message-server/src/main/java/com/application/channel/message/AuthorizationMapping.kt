package com.application.channel.message

import com.application.channel.core.model.ChannelContext

/**
 * @author liuzhongao
 * @since 2024/5/30 00:37
 */
interface AuthorizationMapping {

    fun putAuthContext(sessionId: String, channelContext: ChannelContext)

    fun findSessionId(channelContext: ChannelContext): String?

    fun findChannelContext(sessionId: String): List<ChannelContext>

    fun findChannelContext(sessionIdList: List<String>): List<ChannelContext>

    fun removeAuthContext(sessionId: String)

    fun removeAuthContext(channelContext: ChannelContext)

    fun removeAuthContext(sessionId: String, channelContext: ChannelContext)
}