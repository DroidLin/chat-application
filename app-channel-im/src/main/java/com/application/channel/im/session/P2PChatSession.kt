package com.application.channel.im.session

import com.application.channel.im.MsgClient
import com.application.channel.im.MsgConnectionService
import com.application.channel.message.MessageReceiveListener
import com.application.channel.message.meta.Message

/**
 * @author liuzhongao
 * @since 2024/6/10 13:39
 */
internal class P2PChatSession(
    context: ChatSessionContext
) :
    AbstractChatSession(context), MessageReceiveListener {

    override fun onCreate() {
        MsgClient.getService(MsgConnectionService::class.java)
            .addListener(this)
    }

    override fun onDestroy() {
        MsgClient.getService(MsgConnectionService::class.java)
            .removeListener(this)
    }

    override fun interceptMessageEvent(message: Message, notify: (Message) -> Unit) {
    }

    override fun close() {
        MsgClient.getService(MsgConnectionService::class.java).closeSession(this)
    }
}