package com.application.channel.im

import com.application.channel.database.AppMessageDatabase

/**
 * @author liuzhongao
 * @since 2024/6/10 11:14
 */
data class IMInitConfig(
    val remoteAddress: String,
    val token: Token,
    val factory: AppMessageDatabase.Factory
)
