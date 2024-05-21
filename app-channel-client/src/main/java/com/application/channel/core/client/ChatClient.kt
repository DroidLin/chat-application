package com.application.channel.core.client

import com.application.channel.core.ChannelContextMatcher
import com.application.channel.core.Listener
import com.application.channel.core.WriteToChannelJob
import com.application.channel.core.model.InitConfig
import com.application.channel.core.model.SimpleSocketInitConfig

/**
 * @author liuzhongao
 * @since 2024/5/11 01:06
 */

fun ChatClient(initConfig: InitConfig): ChatClient = ChatClientImpl(initConfig)

interface ChatClient {

    val initConfig: InitConfig

    fun start(connectListener: ConnectListener)
    fun writeValue(value: Any?)
    fun writeValue(value: Any?, listener: Listener?)
    fun writeValue(value: Any?, channelContextMatcher: ChannelContextMatcher)
    fun writeValue(value: Any?, channelContextMatcher: ChannelContextMatcher, listener: Listener?)
    fun shutDown()
}

private class ChatClientImpl(override val initConfig: InitConfig) : ChatClient {

    private val _clientApp: AppClient = AppClient(this.initConfig)

    override fun start(connectListener: ConnectListener) {
        this._clientApp.run(connectListener)
    }

    override fun writeValue(value: Any?) {
        this.writeValue(value, ChannelContextMatcher.all())
    }

    override fun writeValue(value: Any?, listener: Listener?) {
        this.writeValue(value, ChannelContextMatcher.all(), listener)
    }

    override fun writeValue(value: Any?, channelContextMatcher: ChannelContextMatcher) {
        this.writeValue(value, channelContextMatcher, null)
    }

    override fun writeValue(value: Any?, channelContextMatcher: ChannelContextMatcher, listener: Listener?) {
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