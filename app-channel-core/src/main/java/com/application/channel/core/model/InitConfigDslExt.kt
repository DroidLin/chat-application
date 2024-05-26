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

    val channelEventListener = object : ChannelEventListener {
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

    lateinit var remoteAddress: String

    var maxReconnectCount: Int = Int.MAX_VALUE

    var reConnectTimeInterval: Long = 5000L

    private  var initAdapter: InitAdapter? = null

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
            channelEventListener = this.channelEventListener,
            initAdapter = this.initAdapter
        )
    }
}

fun multiInitConfig(function:  MultiInitScope.() -> Unit): MultiInitConfig {
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