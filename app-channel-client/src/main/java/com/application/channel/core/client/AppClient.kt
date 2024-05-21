package com.application.channel.core.client

import com.application.channel.core.model.InitConfig
import com.application.channel.core.model.MultiInitConfig
import com.application.channel.core.model.SimpleSocketInitConfig
import com.application.channel.core.model.channelContext
import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelOption
import io.netty.channel.group.ChannelGroup
import io.netty.channel.group.DefaultChannelGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
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
            .channel(NioSocketChannel::class.java)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30000)
            .option(ChannelOption.SO_KEEPALIVE, true)
    }

    fun run(connectListener: ConnectListener) {
        val simpleInitConfigList = this.initConfig.let { initConfig ->
            when (initConfig) {
                is SimpleSocketInitConfig -> listOf(initConfig)
                is MultiInitConfig -> initConfig.initConfigList
                else -> throw IllegalArgumentException("unsupported init config: $initConfig")
            }
        }
        val isAllConnected = simpleInitConfigList.all { initConfig -> initConfig.isRunning }
        if (isAllConnected) {
            simpleInitConfigList.forEach(connectListener::onConnected)
            return
        }
        simpleInitConfigList.filter { initConfig -> !initConfig.isRunning }
            .forEach { initConfig -> this.doConnect(initConfig, connectListener) }
    }

    fun scheduleInEventLoop(job: () -> Unit) {
        this.eventLoop.schedule(job, 0, TimeUnit.SECONDS)
    }

    private fun doConnect(initConfig: SimpleSocketInitConfig, connectListener: ConnectListener) {
        initConfig.isRunning = false
        val channelFutureListener = object : ChannelFutureListener {
            override fun operationComplete(future: ChannelFuture?) {
                if (future == null) return
                if (!future.isSuccess) {
                    initConfig.isRunning = false
                    initConfig.channelEventListener?.handleException(
                        ctx = future.channel().channelContext,
                        throwable = future.cause()
                    )
                    connectListener.onFailure(future.cause())
                } else {
                    initConfig.isRunning = true
                    initConfig.nowReConnectCount = 0
                    connectListener.onConnected(initConfig)
                }
            }
        }
        val channelInitializer = ChatClientInitializer(this._channelGroup, initConfig.initAdapter)
        val socketAddress = initConfig.socketAddress
        this.bootstrap
            .handler(channelInitializer)
            .connect(socketAddress)
            .addListener(channelFutureListener)
    }

    fun shutDown() {
        this.eventLoop.shutdownGracefully()
        this._channelGroup.close().sync()
        this._channelGroup.clear()
    }
}