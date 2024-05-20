package com.application.channel.core.model

import com.application.channel.core.ChannelEventListener
import com.application.channel.core.InitAdapter
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.net.URI

/**
 * @author liuzhongao
 * @since 2024/5/18 18:08
 */
sealed interface InitConfig

data class SimpleSocketInitConfig @JvmOverloads constructor(
    val remoteAddress: String,
    val maxReConnectCount: Int = Int.MAX_VALUE,
    /**
     * interval between failure and next reconnect.
     */
    val reConnectTimeInterval: Long = 5000L,

    val channelEventListener: ChannelEventListener? = null,

    val initAdapter: InitAdapter? = null
) : InitConfig {

    var nowReConnectCount: Int = 0

    val socketAddress: SocketAddress by lazy {
        val uri = URI.create(this.remoteAddress)
        val host: String? = uri.host
        val port = uri.port
        if (host.isNullOrBlank() || port == -1) {
            throw IllegalArgumentException("illegal remote address: ${this.remoteAddress}")
        }
        InetSocketAddress(host, port)
    }
}

data class MultiInitConfig constructor(
    val initConfigList: List<SimpleSocketInitConfig>
) : InitConfig