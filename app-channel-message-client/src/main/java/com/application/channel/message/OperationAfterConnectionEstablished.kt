package com.application.channel.message

import com.application.channel.core.ChannelContextMatcher
import com.application.channel.core.client.ChannelClient
import com.application.channel.message.meta.Messages

/**
 * @author liuzhongao
 * @since 2024/6/2 09:56
 */
internal class OperationAfterConnectionEstablished(
    private val channelClient: ChannelClient,
    private val account: Account,
) : Runnable {

    override fun run() {
        val loginMessage = Messages.buildLoginMessage(
            sessionId = this.account.sessionId,
            accountId = this.account.accountId
        )
        this.channelClient.writeValue(
            value = MessageWritable(loginMessage),
            channelContextMatcher = ChannelContextMatcher.all
        )
    }
}