package com.chat.compose.app

import com.application.channel.im.IMDatabaseInitConfig
import com.application.channel.im.IMInitConfig
import com.application.channel.im.SingleIMManager
import com.application.channel.message.Account
import com.chat.compose.app.di.platformAppDatabaseFactory
import com.chat.compose.app.storage.MutableMapStorage

/**
 * @author liuzhongao
 * @since 2024/6/23 23:35
 */

fun initChatService() {
    val mutablePreference = MutableMapStorage("im.json")
    val sessionId = mutablePreference.getString("sessionId", "")
    val accountId = mutablePreference.getString("accountId", "")
    if (sessionId.isNullOrEmpty() || accountId.isNullOrEmpty()) {
        return
    }
    val account = Account(sessionId = sessionId, accountId = accountId)
    val initConfig = IMDatabaseInitConfig(
        account = account,
        factory = platformAppDatabaseFactory()
    )
    SingleIMManager.initDatabase(initConfig)
}

fun startChatService() {
    val mutablePreference = MutableMapStorage("im.json")
    val sessionId = mutablePreference.getString("sessionId", "")
    val accountId = mutablePreference.getString("accountId", "")
    val remoteAddress = mutablePreference.getString("remoteAddress", "")
    if (sessionId.isNullOrEmpty() || accountId.isNullOrEmpty() || remoteAddress.isNullOrEmpty()) {
        return
    }
    val account = Account(sessionId = sessionId, accountId = accountId)
    val initConfig = IMInitConfig(
        remoteAddress = remoteAddress,
        account = account
    )
    SingleIMManager.startService(initConfig)
}

fun initAndStartChatService() {
    val mutablePreference = MutableMapStorage("im.json")
    val sessionId = mutablePreference.getString("sessionId", "")
    val accountId = mutablePreference.getString("accountId", "")
    if (sessionId.isNullOrEmpty() || accountId.isNullOrEmpty()) {
        return
    }
    val account = Account(sessionId = sessionId, accountId = accountId)
    val databaseInitConfig = IMDatabaseInitConfig(
        account = account,
        factory = platformAppDatabaseFactory()
    )
    SingleIMManager.initDatabase(databaseInitConfig)

    val remoteAddress = mutablePreference.getString("remoteAddress", "")
    if (remoteAddress.isNullOrEmpty()) {
        return
    }
    val initConfig = IMInitConfig(
        remoteAddress = remoteAddress,
        account = account
    )
    SingleIMManager.startService(initConfig)
}