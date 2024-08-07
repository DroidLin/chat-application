package com.application.channel.core.server

import com.application.channel.core.InitAdapter
import java.net.SocketAddress

/**
 * @author liuzhongao
 * @since 2024/8/5 22:48
 */
data class InnerChannelInitConfig(
    val initAdapter: InitAdapter,
    val socketAddress: SocketAddress
)
