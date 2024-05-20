package com.application.channel.core.client

import com.application.channel.core.ChannelEventListener
import com.application.channel.core.model.*
import com.application.channel.core.plus
import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelOption
import io.netty.channel.group.ChannelGroup
import io.netty.channel.group.DefaultChannelGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.util.concurrent.GlobalEventExecutor
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

/**
 * @author liuzhongao
 * @since 2024/5/10 00:32
 */
internal class AppClient(private val initConfig: InitConfig) {

    private val _channelGroup = DefaultChannelGroup(GlobalEventExecutor.INSTANCE)
    private val _isRunningStateMap = ConcurrentHashMap<SimpleSocketInitConfig, Boolean>()

    private val bootstrap = Bootstrap()
    private val eventLoop = NioEventLoopGroup()

    private var reconnectCount: Int = 0

    val channelGroup: ChannelGroup get() = this._channelGroup

    private val channelEventListener: ChannelEventListener? by lazy {
        when (this.initConfig) {
            is SimpleSocketInitConfig -> object : ChannelEventListener {
                override fun handleConnectionLoss(ctx: ChannelContext) {
                    this@AppClient.eventLoop.schedule(
                        {
                            doConnect(this@AppClient.initConfig) { this@AppClient.reconnectCount = 0 }
                        }, 5L, TimeUnit.SECONDS
                    )
                }
            } + this@AppClient.initConfig.channelEventListener
            is MultiInitConfig -> ChannelEventListener(this.initConfig)
            else -> throw IllegalArgumentException("unsupported init config: ${this.initConfig}")
        }
    }

    init {
        this.bootstrap.group(this.eventLoop)
            .channel(NioSocketChannel::class.java)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30000)
            .option(ChannelOption.SO_KEEPALIVE, true)
    }

    fun run(doOnConnected: (SimpleSocketInitConfig) -> Unit) {
        val simpleSocketInitConfigList = this.initConfig.let { initConfig ->
            when (initConfig) {
                is SimpleSocketInitConfig -> listOf(initConfig)
                is MultiInitConfig -> initConfig.initConfigList
                else -> throw IllegalArgumentException("unsupported init config: $initConfig")
            }
        }
        val isAllConnected = simpleSocketInitConfigList.map { simpleInitConfig -> this._isRunningStateMap[simpleInitConfig] == true }
            .reduce { one, other -> one && other }
        if (isAllConnected) {
            simpleSocketInitConfigList.forEach(doOnConnected)
            return
        }
        simpleSocketInitConfigList.filter { initConfig -> this._isRunningStateMap[initConfig] != true }
            .forEach { initConfig -> this.doConnect(initConfig, doOnConnected) }
    }

    fun scheduleInEventLoop(job: () -> Unit) {
        this.eventLoop.schedule(job, 0, TimeUnit.SECONDS)
    }

    private fun doConnect(simpleSocketInitConfig: SimpleSocketInitConfig, doOnConnected: (SimpleSocketInitConfig) -> Unit = {}) {
        this._isRunningStateMap[simpleSocketInitConfig] = false
        val channelFutureListener = object : ChannelFutureListener {
            override fun operationComplete(future: ChannelFuture?) {
                if (future == null) return
                if (!future.isSuccess) {
                    this@AppClient._isRunningStateMap[simpleSocketInitConfig] = false
                    this@AppClient.channelEventListener?.handleException(future.channel().channelContext, future.cause())
                    val shouldScheduleReConnect = (++simpleSocketInitConfig.nowReConnectCount <= simpleSocketInitConfig.maxReConnectCount)
                    if (shouldScheduleReConnect) {
                        future.channel().eventLoop().schedule(
                            {
                                doConnect(simpleSocketInitConfig) { this@AppClient.reconnectCount = 0 }
                            }, 5L, TimeUnit.SECONDS
                        )
                    }
                } else {
                    this@AppClient._isRunningStateMap[simpleSocketInitConfig] = true
                    doOnConnected(simpleSocketInitConfig)
                }
            }
        }
        val channelInitializer = ChatClientInitializer(this._channelGroup, simpleSocketInitConfig.initAdapter)
        val socketAddress = simpleSocketInitConfig.socketAddress
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