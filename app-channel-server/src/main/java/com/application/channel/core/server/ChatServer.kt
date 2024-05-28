package com.application.channel.core.server

import com.application.channel.core.ChannelContextMatcher
import com.application.channel.core.Listener
import com.application.channel.core.WriteToChannelJob
import com.application.channel.core.model.InitConfig
import com.application.channel.core.model.Writable

/**
 * @author liuzhongao
 * @since 2024/5/14 23:27
 */
fun ChatServer(config: InitConfig): ChatServer = ChatServerImpl(config)

interface ChatServer {

    val initConfig: InitConfig

    fun start()

    fun triggerReconnect(initConfig: InitConfig)

    fun write(value: Writable)

    fun write(value: Writable, matcher: ChannelContextMatcher)

    fun write(value: Writable, matcher: ChannelContextMatcher, listener: Listener?)

    fun shutDown()

    fun shutDown(initConfig: InitConfig)

}

private class ChatServerImpl(override val initConfig: InitConfig) : ChatServer {

    private val appServer = AppServer(this.initConfig)

    override fun start() {
        this.appServer.run()
    }

    override fun triggerReconnect(initConfig: InitConfig) {
        this.appServer.run(initConfig)
    }

    override fun write(value: Writable) {
        this.write(value, ChannelContextMatcher.all())
    }

    override fun write(value: Writable, matcher: ChannelContextMatcher) {
        this.write(value, matcher, null)
    }

    override fun write(value: Writable, matcher: ChannelContextMatcher, listener: Listener?) {
        val writeToChannelJob = WriteToChannelJob(
            channelGroup = this.appServer.channelGroup,
            value = value,
            channelMatcher = matcher,
            listener = listener
        )
        this.appServer.scheduleInEventLoop(job = writeToChannelJob::run)
    }

    override fun shutDown() {
        this.appServer.shutDown()
    }

    override fun shutDown(initConfig: InitConfig) {
        this.appServer.shutDown(initConfig)
    }
}