package com.application.channel.message

import com.application.channel.message.meta.Message
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author liuzhongao
 * @since 2024/6/1 16:38
 */
fun interface MessageReceiveListener {

    fun onReceive(message: Message)
}

@Singleton
class MessageReceiveListenerWrapper @Inject constructor() : MessageReceiveListener {

    private val listeners = mutableListOf<MessageReceiveListener>()

    fun addListener(listener: MessageReceiveListener) {
        synchronized(this.listeners) {
            if (!this.listeners.contains(listener)) {
                this.listeners.add(listener)
            }
        }
    }

    fun removeListener(listener: MessageReceiveListener) {
        synchronized(this.listeners) {
            if (this.listeners.contains(listener)) {
                this.listeners.remove(listener)
            }
        }
    }

    override fun onReceive(message: Message) {
        val newCopyList = synchronized(this.listeners) {
            this.listeners.toList()
        }
        newCopyList.forEach { listener -> listener.onReceive(message) }
    }

    fun release() {
        synchronized(this.listeners) {
            this.listeners.clear()
        }
    }
}