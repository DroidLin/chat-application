package com.application.channel.message

import com.application.channel.database.AppMessageDatabase

/**
 * @author liuzhongao
 * @since 2024/6/23 12:29
 */
data class ChatServiceDatabaseInitConfig(
    val account: Account,
    val factory: AppMessageDatabase.Factory
)