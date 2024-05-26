package com.application.channel.core.client

import com.application.channel.core.initializer.ChannelInitializerFactory
import com.application.channel.core.model.InitConfig
import com.application.channel.core.model.MultiInitConfig
import com.application.channel.core.model.SocketChannelInitConfig
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
    }

    fun run(doOnConnected: (InitConfig) -> Unit) {
        val initFunction =  { bootstrap: Bootstrap, initConfig: InitConfig ->
            if (initConfig is SocketChannelInitConfig) {
                val channelFutureListener = object : ChannelFutureListener {
                    override fun operationComplete(future: ChannelFuture?) {
                        if (future == null) return
                        if (!future.isSuccess) {
                            initConfig.isRunning = false
                            initConfig.channelEventListener?.handleConnectionLoss(future.channel().channelContext)
                            initConfig.channelEventListener?.handleException(
                                ctx = future.channel().channelContext,
                                throwable = future.cause()
                            )
                        } else {
                            initConfig.isRunning = true
                            initConfig.nowReConnectCount = 0
                            doOnConnected(initConfig)
                        }
                    }
                }
                val socketAddress = initConfig.socketAddress
                bootstrap.connect(socketAddress)
                    .addListener(channelFutureListener)
            }
        }
        ChannelInitializerFactory.create(this._channelGroup, this.initConfig)
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