package com.application.channel.core.client

import com.application.channel.core.initializer.ChannelInitializerFactory
import com.application.channel.core.model.DatagramChannelInitConfig
import com.application.channel.core.model.InitConfig
import com.application.channel.core.model.SocketChannelInitConfig
import com.application.channel.core.model.channelContext
import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelFutureListener
import io.netty.channel.group.ChannelGroup
import io.netty.channel.group.DefaultChannelGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.util.concurrent.GlobalEventExecutor
import java.util.concurrent.TimeUnit

/**
 * @author liuzhongao
 * @since 2024/5/10 00:32
 */
internal class AppClient(private val initConfig: InitConfig) {

    private val _channelGroup = DefaultChannelGroup(GlobalEventExecutor.INSTANCE)

    private val bootstrap = Bootstrap()
    private val eventLoop = NioEventLoopGroup()

    val channelGroup: ChannelGroup get() = this._channelGroup

    init {
        this.bootstrap.group(this.eventLoop)
    }

    fun run(doOnConnected: (InitConfig) -> Unit) = this.run(this.initConfig, doOnConnected)

    fun run(initConfig: InitConfig, doOnConnected: (InitConfig) -> Unit) {
        val initFunction =  { bootstrap: Bootstrap, _initConfig: InitConfig ->
            if (_initConfig is SocketChannelInitConfig) {
                val channelFutureListener = object : ChannelFutureListener {
                    override fun operationComplete(future: ChannelFuture?) {
                        if (future == null) return
                        if (!future.isSuccess) {
                            _initConfig.isRunning = false
                            _initConfig.socketChannelEventListener?.handleConnectionLoss(future.channel().channelContext)
                            _initConfig.socketChannelEventListener?.handleException(future.channel().channelContext, future.cause())
                        } else {
                            _initConfig.isRunning = true
                            _initConfig.nowReConnectCount = 0
                            doOnConnected(initConfig)
                        }
                    }
                }
                bootstrap.connect(_initConfig.socketAddress)
                    .addListener(channelFutureListener)
            } else if (_initConfig is DatagramChannelInitConfig) {
                val channelFutureListener = object : ChannelFutureListener {
                    override fun operationComplete(future: ChannelFuture?) {
                        if (future == null) return
                        if (!future.isSuccess) {
                            _initConfig.datagramChannelEventListener?.handleException(future.channel().channelContext, future.cause())
                        } else {
                            doOnConnected(initConfig)
                        }
                    }
                }
                bootstrap.bind()
                    .addListener(channelFutureListener)
            }
            Unit
        }
        ChannelInitializerFactory.create(this._channelGroup, initConfig)
            .initialize(this.bootstrap, initFunction)
    }

    fun scheduleInEventLoop(job: () -> Unit) {
        this.eventLoop.schedule(job, 0, TimeUnit.SECONDS)
    }

    fun shutDown() {
        this.eventLoop.shutdownGracefully()
        this._channelGroup.close().sync()
        this._channelGroup.clear()
    }
}