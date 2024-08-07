package com.application.channel.core.model

import com.application.channel.core.*

fun socketInitConfig(functionScope: SocketChannelInitScope.() -> Unit): SocketChannelInitConfig {
    return SocketChannelInitScope().apply(functionScope).build()
}

class SocketChannelInitScope internal constructor() {

    private var functionConnectionEstablished: ((ChannelContext) -> Unit)? = null
    private var functionOnNewValueArrived: ((ChannelContext, Any?) -> Unit)? = null
    private var functionConnectionLoss: ((ChannelContext) -> Unit)? = null
    private var functionExceptionCreated: ((ChannelContext, Throwable?) -> Unit)? = null

    val socketChannelEventListener = object : SocketChannelEventListener {
        override fun handleConnectionEstablished(ctx: ChannelContext) {
            this@SocketChannelInitScope.functionConnectionEstablished?.invoke(ctx)
        }

        override fun handleValueRead(ctx: ChannelContext, value: Any?) {
            this@SocketChannelInitScope.functionOnNewValueArrived?.invoke(ctx, value)
        }

        override fun handleConnectionLoss(ctx: ChannelContext) {
            this@SocketChannelInitScope.functionConnectionLoss?.invoke(ctx)
        }

        override fun handleException(ctx: ChannelContext, throwable: Throwable?) {
            this@SocketChannelInitScope.functionExceptionCreated?.invoke(ctx, throwable)
        }
    }

    private var functionChannelAttached: ((ChannelContext) -> Unit)? = null
    private var functionChannelDetached: ((ChannelContext) -> Unit)? = null

    val channelLifecycleObserver = object : ChannelLifecycleObserver {
        override fun onChannelAttached(channel: ChannelContext) {
            this@SocketChannelInitScope.functionChannelAttached?.invoke(channel)
        }

        override fun onChannelDetached(channel: ChannelContext) {
            this@SocketChannelInitScope.functionChannelDetached?.invoke(channel)
        }
    }

    private lateinit var address: String
    private var maxReconnectCount: Int = Int.MAX_VALUE
    private var reConnectTimeInterval: Long = 5000L
    private var initAdapter: InitAdapter? = null

    fun address(remoteAddress: String) {
        this.address = remoteAddress
    }

    fun maxReconnectCount(number: Int) {
        this.maxReconnectCount = number
    }

    fun reconnectTimeInterval(interval: Long) {
        this.reConnectTimeInterval = interval
    }

    fun onConnectionEstablished(function: (ChannelContext) -> Unit) {
        this.functionConnectionEstablished = function
    }

    fun afterNewValueRead(function: (ChannelContext, Any?) -> Unit) {
        this.functionOnNewValueArrived = function
    }

    fun onConnectionLoss(function: (ChannelContext) -> Unit) {
        this.functionConnectionLoss = function
    }

    fun onExceptionCreated(function: (ChannelContext, Throwable?) -> Unit) {
        this.functionExceptionCreated = function
    }

    fun initAdapter(function: InitialAdapterBuilder.() -> Unit) {
        this.initAdapter = InitialAdapterBuilder().apply(function).build()
    }

    fun channelAttached(function: (ChannelContext) -> Unit) {
        this.functionChannelAttached = function
    }

    fun channelDetached(function: (ChannelContext) -> Unit) {
        this.functionChannelDetached = function
    }

    fun build(): SocketChannelInitConfig {
        return SocketChannelInitConfig(
            address = this.address,
            maxReConnectCount = this.maxReconnectCount,
            reConnectTimeInterval = this.reConnectTimeInterval,
            socketChannelEventListener = this.socketChannelEventListener,
            initAdapter = requireNotNull(this.initAdapter)
        )
    }
}

fun datagramInitConfig(function: DatagramInitScope.() -> Unit): DatagramChannelInitConfig {
    return DatagramInitScope().apply(function).build()
}

class DatagramInitScope internal constructor() {

    private var localAddress: String = ""
    private var remoteAddress: String = ""
    private var initAdapter: InitAdapter? = null
    private var broadcast: Boolean = false

    private var functionOnNewValueArrived: ((ChannelContext, Any?) -> Unit)? = null
    private var functionExceptionCreated: ((ChannelContext, Throwable?) -> Unit)? = null

    val datagramChannelEventListener = object : DatagramChannelEventListener {
        override fun handleValueRead(ctx: ChannelContext, value: Any?) {
            this@DatagramInitScope.functionOnNewValueArrived?.invoke(ctx, value)
        }

        override fun handleException(ctx: ChannelContext, throwable: Throwable?) {
            this@DatagramInitScope.functionExceptionCreated?.invoke(ctx, throwable)
        }
    }

    fun localAddress(address: String) {
        this.localAddress = address
    }

    fun remoteAddress(address: String) {
        this.remoteAddress = address
    }

    fun afterNewValueRead(function: (ChannelContext, Any?) -> Unit) {
        this.functionOnNewValueArrived = function
    }

    fun broadcast(enable: Boolean) {
        this.broadcast = enable
    }

    fun onExceptionCreated(function: (ChannelContext, Throwable?) -> Unit) {
        this.functionExceptionCreated = function
    }

    fun initAdapter(function: InitialAdapterBuilder.() -> Unit) {
        this.initAdapter = InitialAdapterBuilder().apply(function).build()
    }

    fun build(): DatagramChannelInitConfig {
        return DatagramChannelInitConfig(
            localAddress = this.localAddress,
            remoteAddress = this.remoteAddress,
            broadcast = this.broadcast,
            datagramChannelEventListener = datagramChannelEventListener,
            initAdapter = initAdapter
        )
    }

}