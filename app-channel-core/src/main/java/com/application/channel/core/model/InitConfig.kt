package com.application.channel.core.model

import com.application.channel.core.DatagramChannelEventListener
import com.application.channel.core.InitAdapter
import com.application.channel.core.SocketChannelEventListener
import com.application.channel.core.parseNetworkAddress
import java.net.SocketAddress

/**
 * @author liuzhongao
 * @since 2024/5/18 18:08
 */
sealed interface InitConfig

data class DatagramChannelInitConfig @JvmOverloads constructor(
    val localAddress: String,
    val remoteAddress: String,
    val broadcast: Boolean = false,
    val datagramChannelEventListener: DatagramChannelEventListener? = null,
    val initAdapter: InitAdapter? = null
) : InitConfig {

    val localSocketAddress: SocketAddress by lazy { parseNetworkAddress(this.localAddress) }
    val remoteSocketAddress: SocketAddress by lazy { parseNetworkAddress(this.localAddress) }
}

data class SocketChannelInitConfig @JvmOverloads constructor(
    val remoteAddress: String,
    val maxReConnectCount: Int = Int.MAX_VALUE,
    /**
     * interval between failure and next reconnect.
     */
    val reConnectTimeInterval: Long = 5000L,

    val socketChannelEventListener: SocketChannelEventListener? = null,

    val initAdapter: InitAdapter? = null
) : InitConfig {

    var nowReConnectCount: Int = 0

    var isRunning: Boolean = false

    val socketAddress: SocketAddress by lazy { parseNetworkAddress(this.remoteAddress) }
}

data class MultiInitConfig constructor(
    val initConfigList: List<InitConfig>
) : InitConfig