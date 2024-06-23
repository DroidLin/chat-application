package com.application.channel.im

import com.application.channel.message.Account

/**
 * @author liuzhongao
 * @since 2024/6/10 11:14
 */
data class IMInitConfig(
    val remoteAddress: String,
    val account: Account,
    val maxReConnectCount: Int = 3
)
