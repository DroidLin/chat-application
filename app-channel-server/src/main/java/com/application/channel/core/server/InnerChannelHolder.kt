package com.application.channel.core.server

import com.application.channel.core.Listener
import com.application.channel.core.WriteChannelContextTask
import com.application.channel.core.model.ChannelContext
import com.application.channel.core.model.Writable
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelOption
import io.netty.channel.WriteBufferWaterMark
import io.netty.channel.group.DefaultChannelGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.util.concurrent.GlobalEventExecutor
import java.util.concurrent.TimeUnit

/**
 * @author liuzhongao
 * @since 2024/8/5 22:32
 */
class InnerChannelHolder(private val initConfig: InnerChannelInitConfig) {

    private val _channelGroup = DefaultChannelGroup(GlobalEventExecutor.INSTANCE)
    private val _parentEventLoop = NioEventLoopGroup(1)
    private val _childEventLoop = NioEventLoopGroup(4)

    @Volatile
    private var initialized = false

    private var channelFuture: ChannelFuture? = null

    fun initChannelServer() {
        if (this.initialized) return
        this.initialized = true

        val channelInitializer = InnerChannelInitializer(
            channelGroup = this._channelGroup,
            initAdapter = this.initConfig.initAdapter
        )

        this.channelFuture = ServerBootstrap()
            .channel(NioServerSocketChannel::class.java)
            .childHandler(channelInitializer)
            .group(this._parentEventLoop, this._childEventLoop)
            .childOption(ChannelOption.SO_KEEPALIVE, true)
            .childOption(ChannelOption.TCP_NODELAY, true)
            .childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, WriteBufferWaterMark(8 * 1024, 32 * 1024))
            .bind(this.initConfig.socketAddress).sync()
    }

    @JvmOverloads
    fun writeChannel(ctx: ChannelContext, writable: Writable, listener: Listener? = null) {
        this.writeChannel(listOf(ctx), writable, listener)
    }

    @JvmOverloads
    fun writeChannel(contextList: List<ChannelContext>, writable: Writable, listener: Listener? = null) {
        val writeToChannelJob = WriteChannelContextTask(
            channelGroup = this._channelGroup,
            channelContextList = contextList,
            value = writable,
            listener = listener
        )
        this.scheduleInEventLoop(runnable = writeToChannelJob::run)
    }

    fun scheduleInEventLoop(runnable: Runnable, delay: Long = 0L) {
        this._childEventLoop.schedule(runnable, delay, TimeUnit.MILLISECONDS)
    }

    fun stopChannelServer() {
        this._channelGroup.close()?.awaitUninterruptibly()
        this._channelGroup.clear()

        this._parentEventLoop.shutdownGracefully()
        this._childEventLoop.shutdownGracefully()

        this.channelFuture?.channel()?.close()?.awaitUninterruptibly()
        this.channelFuture = null
    }
}