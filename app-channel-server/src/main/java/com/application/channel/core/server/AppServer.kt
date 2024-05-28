package com.application.channel.core.server

import com.application.channel.core.initializer.ChannelInitializerFactory
import com.application.channel.core.model.*
import io.netty.bootstrap.AbstractBootstrap
import io.netty.bootstrap.Bootstrap
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.group.ChannelGroup
import io.netty.channel.group.DefaultChannelGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.util.concurrent.GlobalEventExecutor
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

/**
 * @author liuzhongao
 * @since 2024/5/8 23:20
 */
internal class AppServer(private val initConfig: InitConfig) {

    private val _channelGroup = DefaultChannelGroup(GlobalEventExecutor.INSTANCE)
    private val _initConfigFutureMap = ConcurrentHashMap<InitConfig, ChannelFuture>()

    private val _parentEventLoop = NioEventLoopGroup()
    private val _childEventLoop = NioEventLoopGroup()

    val channelGroup: ChannelGroup get() = this._channelGroup

    fun run() = this.run(this.initConfig)

    fun run(initConfig: InitConfig) {
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
        ChannelInitializerFactory.create(this._channelGroup, initConfig)
            .also { initializer -> initializer.initialize(ServerBootstrap().group(this._parentEventLoop, this._childEventLoop), initFunction) }
            .also { initializer -> initializer.initialize(Bootstrap().group(this._parentEventLoop), initFunction) }
    }

    fun scheduleInEventLoop(job: () -> Unit) {
        this._parentEventLoop.schedule(job, 0, TimeUnit.SECONDS)
    }

    fun shutDown() {
        this._parentEventLoop.shutdownGracefully()
        this._childEventLoop.shutdownGracefully()
        this._channelGroup.close().sync()
        this._channelGroup.clear()
    }

    fun shutDown(initConfig: InitConfig) {
        this._initConfigFutureMap.remove(initConfig)?.channel()?.close()?.sync()
    }
}