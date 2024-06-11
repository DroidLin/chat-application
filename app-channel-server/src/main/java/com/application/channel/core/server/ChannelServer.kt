package com.application.channel.core.server

import com.application.channel.core.ChannelContextMatcher
import com.application.channel.core.Listener
import com.application.channel.core.WriteToChannelJob
import com.application.channel.core.initializer.ChannelInitializerFactory
import com.application.channel.core.model.*
import io.netty.bootstrap.AbstractBootstrap
import io.netty.bootstrap.Bootstrap
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.group.ChannelGroup
import io.netty.channel.nio.NioEventLoopGroup
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author liuzhongao
 * @since 2024/5/14 23:27
 */
fun ChannelServer(): ChannelServer {
    return DaggerChannelServerComponent.builder()
        .build().channelServer()
}

interface ChannelServer {

    fun isChannelAlive(channelContext: ChannelContext): Boolean

    fun start(initConfig: InitConfig)

    fun write(value: Writable)

    fun write(value: Writable, matcher: ChannelContextMatcher)

    fun write(value: Writable, matcher: ChannelContextMatcher, listener: Listener?)

    fun shutDown()

    fun shutDown(initConfig: InitConfig)
}

internal class ChannelServerImpl @Inject constructor(
    private val channelGroup: ChannelGroup
) : ChannelServer {

    private val _initConfigFutureMap = ConcurrentHashMap<InitConfig, ChannelFuture>()

    private val _parentEventLoop = NioEventLoopGroup(1)
    private val _childEventLoop = NioEventLoopGroup(4)

    private fun run(initConfig: InitConfig) {
        val initFunction = { bootstrap: AbstractBootstrap<*, *>, _initConfig: InitConfig ->
            if (_initConfig is SocketChannelInitConfig && bootstrap is ServerBootstrap) {
                val socketAddress = _initConfig.socketAddress
                val channelFuture = bootstrap.bind(socketAddress).sync()
                this._initConfigFutureMap[_initConfig] = channelFuture
            } else if (_initConfig is DatagramChannelInitConfig) {
                val socketAddress = _initConfig.localSocketAddress
                val channelFuture = bootstrap.localAddress(socketAddress).bind().sync()
                this._initConfigFutureMap[initConfig] = channelFuture
            }
        }
        ChannelInitializerFactory.create(this.channelGroup, initConfig)
            .also { initializer -> initializer.initialize(ServerBootstrap().group(this._parentEventLoop, this._childEventLoop), initFunction) }
            .also { initializer -> initializer.initialize(Bootstrap().group(this._parentEventLoop), initFunction) }
    }

    private fun scheduleInEventLoop(job: () -> Unit) {
        this._parentEventLoop.schedule(job, 0, TimeUnit.SECONDS)
    }

    override fun isChannelAlive(channelContext: ChannelContext): Boolean {
        return this.channelGroup.find { channel -> channel.channelId == channelContext.channelId }?.isActive == true
    }

    override fun start(initConfig: InitConfig) {
        this.run(initConfig)
    }

    override fun write(value: Writable) {
        this.write(value, ChannelContextMatcher.all)
    }

    override fun write(value: Writable, matcher: ChannelContextMatcher) {
        this.write(value, matcher, null)
    }

    override fun write(value: Writable, matcher: ChannelContextMatcher, listener: Listener?) {
        val writeToChannelJob = WriteToChannelJob(
            channelGroup = this.channelGroup,
            value = value,
            channelMatcher = matcher,
            listener = listener
        )
        this.scheduleInEventLoop(job = writeToChannelJob::run)
    }

    override fun shutDown() {
        this._parentEventLoop.shutdownGracefully()
        this._childEventLoop.shutdownGracefully()
        this.channelGroup.close().sync()
        this.channelGroup.clear()
    }

    override fun shutDown(initConfig: InitConfig) {
        this._initConfigFutureMap.remove(initConfig)?.channel()?.close()?.sync()
    }
}