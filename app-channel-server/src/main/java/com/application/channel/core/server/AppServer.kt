package com.application.channel.core.server

import com.application.channel.core.ChannelEventListener
import com.application.channel.core.model.InitConfig
import com.application.channel.core.model.MultiInitConfig
import com.application.channel.core.model.SimpleSocketInitConfig
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelOption
import io.netty.channel.group.ChannelGroup
import io.netty.channel.group.DefaultChannelGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.util.concurrent.GlobalEventExecutor
import java.util.concurrent.TimeUnit

/**
 * @author liuzhongao
 * @since 2024/5/8 23:20
 */
internal class AppServer(private val initConfig: InitConfig) {

    private val _channelGroup = DefaultChannelGroup(GlobalEventExecutor.INSTANCE)

    private val _serverBootstrap = ServerBootstrap()
    private val _parentEventLoop = NioEventLoopGroup()
    private val _childEventLoop = NioEventLoopGroup()

    val channelGroup: ChannelGroup get() = this._channelGroup

    init {
        this._serverBootstrap.group(this._parentEventLoop, this._childEventLoop)
            .channel(NioServerSocketChannel::class.java)
            .option(ChannelOption.SO_BACKLOG, 128)
            .childOption(ChannelOption.SO_KEEPALIVE, true)
    }

    fun run() {
        when (this.initConfig) {
            is SimpleSocketInitConfig -> {
                val childChannelInitializer = ChatServerInitializer(
                    channelGroup = this.channelGroup,
                    initAdapter = this.initConfig.initAdapter
                )
                val socketAddress = this.initConfig.socketAddress
                this._serverBootstrap
                    .childHandler(childChannelInitializer)
                    .bind(socketAddress).sync()
            }
            is MultiInitConfig -> {
                this.initConfig.initConfigList.forEach { simpleInitConfig ->
                    val childChannelInitializer = ChatServerInitializer(
                        channelGroup = this.channelGroup,
                        initAdapter = simpleInitConfig.initAdapter
                    )
                    val socketAddress = simpleInitConfig.socketAddress
                    this._serverBootstrap
                        .childHandler(childChannelInitializer)
                        .bind(socketAddress).sync()
                }
            }
            else -> {}
        }
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
}