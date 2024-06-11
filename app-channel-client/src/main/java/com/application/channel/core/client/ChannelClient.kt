package com.application.channel.core.client

import com.application.channel.core.ChannelContextMatcher
import com.application.channel.core.Listener
import com.application.channel.core.WriteToChannelJob
import com.application.channel.core.initializer.ChannelInitializerFactory
import com.application.channel.core.model.*
import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelFutureListener
import io.netty.channel.group.ChannelGroup
import io.netty.channel.nio.NioEventLoopGroup
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * provide [ChannelClient] using dagger component, or dependencies can use
 * [ChannelClientComponent] to generate instance automatically.
 */
fun ChannelClient(): ChannelClient {
    return DaggerChannelClientComponent.builder()
        .build()
        .channelClient()
}

interface ChannelClient {

    fun scheduleInEventLoop(runnable: Runnable)

    fun scheduleInEventLoop(runnable: Runnable, timeout: Long)

    /**
     * start connect to remote address corresponding to the address passed in [initConfig]
     */
    fun start(initConfig: InitConfig, doOnConnected: (InitConfig) -> Unit)

    /**
     * write data to remote without any notifications or callbacks
     * and assume all the remote devices will receive this message
     */
    fun writeValue(value: Writable)

    /**
     * write data to remote devices, and passing [listener] to known
     * whether this data is being written successfully
     */
    fun writeValue(value: Writable, listener: Listener?)

    /**
     * write data to a specific remote devices indexed by [channelContextMatcher]
     */
    fun writeValue(value: Writable, channelContextMatcher: ChannelContextMatcher)

    fun writeValue(value: Writable, channelContextMatcher: ChannelContextMatcher, listener: Listener?)

    /**
     * dismiss connection to remote devices, all the operations that is not finished will be dropped.
     */
    fun shutDown()
}

internal class ChannelClientImpl @Inject constructor(
    private val _channelGroup: ChannelGroup
) : ChannelClient {

    private val eventLoop = NioEventLoopGroup(4)
    private val channelGroup: ChannelGroup get() = this._channelGroup

    private fun run(initConfig: InitConfig, doOnConnected: (InitConfig) -> Unit) {
        val initFunction = { bootstrap: Bootstrap, config: InitConfig ->
            if (config is SocketChannelInitConfig) {
                this.connectTCPService(bootstrap, config, doOnConnected)
            } else if (config is DatagramChannelInitConfig) {
                this.bindUDPService(bootstrap, config, doOnConnected)
            }
        }
        val bootstrap = Bootstrap().also { bootstrap -> bootstrap.group(this.eventLoop) }
        ChannelInitializerFactory.create(this._channelGroup, initConfig)
            .initialize(bootstrap, initFunction)
    }

    private fun connectTCPService(
        bootstrap: Bootstrap,
        initConfig: SocketChannelInitConfig,
        doOnConnected: (SocketChannelInitConfig) -> Unit
    ) {
        val channelFutureListener = object : ChannelFutureListener {
            override fun operationComplete(future: ChannelFuture?) {
                if (future == null) return
                if (!future.isSuccess) {
                    initConfig.isRunning = false
                    val eventListener = initConfig.socketChannelEventListener
                    eventListener?.handleConnectionLoss(future.channel().channelContext)
                    eventListener?.handleException(future.channel().channelContext, future.cause())
                } else {
                    initConfig.isRunning = true
                    doOnConnected(initConfig)
                }
            }
        }
        bootstrap.connect(initConfig.socketAddress)
            .addListener(channelFutureListener)
    }

    private fun bindUDPService(
        bootstrap: Bootstrap,
        initConfig: DatagramChannelInitConfig,
        doOnConnected: (InitConfig) -> Unit
    ) {
        val channelFutureListener = object : ChannelFutureListener {
            override fun operationComplete(future: ChannelFuture?) {
                if (future == null) return
                if (!future.isSuccess) {
                    val eventListener = initConfig.datagramChannelEventListener
                    eventListener?.handleException(future.channel().channelContext, future.cause())
                } else {
                    doOnConnected(initConfig)
                }
            }
        }
        bootstrap.bind().addListener(channelFutureListener)
    }

    private fun scheduleInEventLoop(job: () -> Unit) {
        this.eventLoop.schedule(job, 0, TimeUnit.MILLISECONDS)
    }

    private fun scheduleInEventLoop(timeout: Long, job: () -> Unit) {
        this.eventLoop.schedule(job, timeout, TimeUnit.MILLISECONDS)
    }

    override fun scheduleInEventLoop(runnable: Runnable) {
        this.scheduleInEventLoop(runnable::run)
    }

    override fun scheduleInEventLoop(runnable: Runnable, timeout: Long) {
        this.scheduleInEventLoop(timeout, runnable::run)
    }

    override fun start(initConfig: InitConfig, doOnConnected: (InitConfig) -> Unit) {
        this.run(initConfig, doOnConnected)
    }

    override fun writeValue(value: Writable) {
        this.writeValue(value, ChannelContextMatcher.all)
    }

    override fun writeValue(value: Writable, listener: Listener?) {
        this.writeValue(value, ChannelContextMatcher.all, listener)
    }

    override fun writeValue(value: Writable, channelContextMatcher: ChannelContextMatcher) {
        this.writeValue(value, channelContextMatcher, null)
    }

    override fun writeValue(value: Writable, channelContextMatcher: ChannelContextMatcher, listener: Listener?) {
        val writeToChannelJob = WriteToChannelJob(
            channelGroup = this.channelGroup,
            value = value,
            channelMatcher = channelContextMatcher,
            listener = listener
        )
        this.scheduleInEventLoop(job = writeToChannelJob::run)
    }

    override fun shutDown() {
        this.eventLoop.shutdownGracefully()
        this._channelGroup.close().sync()
        this._channelGroup.clear()
    }
}