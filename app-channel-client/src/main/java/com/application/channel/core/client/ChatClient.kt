package com.application.channel.core.client

import com.application.channel.core.ChannelContextMatcher
import com.application.channel.core.Listener
import com.application.channel.core.WriteToChannelJob
import com.application.channel.core.model.InitConfig
import com.application.channel.core.model.Writable

/**
 * @author liuzhongao
 * @since 2024/5/11 01:06
 */

fun ChatClient(): ChatClient = ChatClientImpl()

interface ChatClient {

    fun start(initConfig: InitConfig, doOnConnected: (InitConfig) -> Unit)
    fun writeValue(value: Writable)
    fun writeValue(value: Writable, listener: Listener?)
    fun writeValue(value: Writable, channelContextMatcher: ChannelContextMatcher)
    fun writeValue(value: Writable, channelContextMatcher: ChannelContextMatcher, listener: Listener?)
    fun shutDown()
}

private class ChatClientImpl : ChatClient {

    private val _clientApp: AppClient = AppClient()

    override fun start(initConfig: InitConfig, doOnConnected: (InitConfig) -> Unit) {
        this._clientApp.run(initConfig, doOnConnected)
    }

    override fun writeValue(value: Writable) {
        this.writeValue(value, ChannelContextMatcher.all())
    }

    override fun writeValue(value: Writable, listener: Listener?) {
        this.writeValue(value, ChannelContextMatcher.all(), listener)
    }

    override fun writeValue(value: Writable, channelContextMatcher: ChannelContextMatcher) {
        this.writeValue(value, channelContextMatcher, null)
    }

    override fun writeValue(value: Writable, channelContextMatcher: ChannelContextMatcher, listener: Listener?) {
        val writeToChannelJob = WriteToChannelJob(
            channelGroup = this._clientApp.channelGroup,
            value = value,
            channelMatcher = channelContextMatcher,
            listener = listener
        )
        this._clientApp.scheduleInEventLoop(job = writeToChannelJob::run)
    }

    override fun shutDown() {
        this._clientApp.shutDown()
    }
}