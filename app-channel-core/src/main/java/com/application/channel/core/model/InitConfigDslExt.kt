package com.application.channel.core.model

import com.application.channel.core.DatagramChannelEventListener
import com.application.channel.core.InitAdapter
import com.application.channel.core.InitialAdapterBuilder
import com.application.channel.core.SocketChannelEventListener

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

    private lateinit var remoteAddress: String
    private var maxReconnectCount: Int = Int.MAX_VALUE
    private var reConnectTimeInterval: Long = 5000L
    private var initAdapter: InitAdapter? = null

    fun remoteAddress(remoteAddress: String) {
        this.remoteAddress = remoteAddress
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

    fun onRemoteConnectionLoss(function: (ChannelContext) -> Unit) {
        this.functionConnectionLoss = function
    }

    fun onExceptionCreated(function: (ChannelContext, Throwable?) -> Unit) {
        this.functionExceptionCreated = function
    }

    fun initAdapter(function: InitialAdapterBuilder.() -> Unit) {
        this.initAdapter = InitialAdapterBuilder().apply(function).build()
    }

    fun build(): SocketChannelInitConfig {
        return SocketChannelInitConfig(
            remoteAddress = this.remoteAddress,
            maxReConnectCount = this.maxReconnectCount,
            reConnectTimeInterval = this.reConnectTimeInterval,
            socketChannelEventListener = this.socketChannelEventListener,
            initAdapter = this.initAdapter
        )
    }
}

fun multiInitConfig(function: MultiInitScope.() -> Unit): MultiInitConfig {
    return MultiInitScope().apply(function).build()
}

class MultiInitScope internal constructor() {

    private val initConfigList: MutableList<InitConfig> = ArrayList()

    fun socketInitConfig(functionScope: SocketChannelInitScope.() -> Unit) {
        val initConfig = SocketChannelInitScope().apply(functionScope).build()
        this.initConfigList += initConfig
    }

    fun build(): MultiInitConfig {
        return MultiInitConfig(this.initConfigList)
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