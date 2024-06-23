package com.application.channel.im

import com.application.channel.database.AppMessageDatabase
import com.application.channel.message.Account

/**
 * @author liuzhongao
 * @since 2024/6/23 12:53
 */
data class IMDatabaseInitConfig(
    val account: Account,
    val factory: AppMessageDatabase.Factory
)