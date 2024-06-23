package com.application.channel.core.client

import com.application.channel.core.InitAdapter
import com.application.channel.core.parseNetworkAddress
import java.net.SocketAddress

/**
 * @author liuzhongao
 * @since 2024/6/23 02:58
 */
data class TcpLocalInitConfig(
    val tcpAddress: String,
    val initAdapter: InitAdapter?,
    val observer: TcpConnectionObserver,
) {

    val socketAddress: SocketAddress? by lazy { parseNetworkAddress(this.tcpAddress) }
}