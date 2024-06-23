package com.application.channel.message

import com.application.channel.core.model.ChannelContext
import com.application.channel.message.meta.LoginResultMessage
import com.application.channel.message.meta.Message
import java.util.logging.Logger

/**
 * @author liuzhongao
 * @since 2024/6/2 10:31
 */
internal class MessageDispatcher : MessageHandler {

    private val messageHandlers = mutableListOf<MessageHandler>()
    private val listeners = MessageReceiveListenerWrapper()

    fun addHandler(handler: MessageHandler) {
        synchronized(this.messageHandlers) {
            if (!this.messageHandlers.contains(handler)) {
                this.messageHandlers.add(handler)
            }
        }
    }

    fun removeHandler(handler: MessageHandler) {
        synchronized(this.messageHandlers) {
            if (this.messageHandlers.contains(handler)) {
                this.messageHandlers.remove(handler)
            }
        }
    }

    fun addReceiveListener(listener: MessageReceiveListener) {
        this.listeners.addListener(listener)
    }

    fun removeReceiveListener(listener: MessageReceiveListener) {
        this.listeners.removeListener(listener)
    }

    override fun handle(channelContext: ChannelContext, message: Message): Boolean {
        return this.handleInner(channelContext, message) {
            this.listeners.onReceive(message)
        }
    }

    private inline fun handleInner(channelContext: ChannelContext, message: Message, function: () -> Unit): Boolean {
        val intercept = synchronized(this.messageHandlers) {
            this.messageHandlers.find { handler ->
                handler.handle(channelContext, message)
            } != null
        }
        if (intercept) {
            return true
        }
        function()
        return false
    }

    fun clear() {
        synchronized(this.messageHandlers) {
            this.messageHandlers.clear()
        }
        this.listeners.clear()
    }
}