package com.chat.compose.app

import com.application.channel.core.util.koinInject
import com.application.channel.im.IMDatabaseInitConfig
import com.application.channel.im.IMInitConfig
import com.application.channel.im.SingleIMManager
import com.application.channel.message.Account
import com.chat.compose.app.di.platformAppDatabaseFactory
import com.chat.compose.app.metadata.isValid
import com.chat.compose.app.network.DEFAULT_HOST
import com.chat.compose.app.network.TCP_PORT
import com.chat.compose.app.services.ProfileService

/**
 * @author liuzhongao
 * @since 2024/6/23 23:35
 */

fun initChatService() {
    val profileService = koinInject<ProfileService>()
    val profile = profileService.profile
    if (!profile.isValid) return

    val sessionId = profile.sessionInfo?.sessionId
    val accountId = profile.account?.accountId
    if (sessionId.isNullOrEmpty() || accountId.isNullOrEmpty()) {
        return
    }
    val account = Account(sessionId = sessionId, accountId = accountId)
    innerInitDatabase(account)
}

fun startChatService() {
    val profileService = koinInject<ProfileService>()
    val profile = profileService.profile
    if (!profile.isValid) return
    val sessionId = profile.sessionInfo?.sessionId
    val accountId = profile.account?.accountId
    val remoteAddress = "http://${DEFAULT_HOST}:${TCP_PORT}"
    if (sessionId.isNullOrEmpty() || accountId.isNullOrEmpty() || remoteAddress.isEmpty()) {
        return
    }
    val account = Account(sessionId = sessionId, accountId = accountId)
    innerStartService(remoteAddress, account)
}

fun initAndStartChatService() {
    val profileService = koinInject<ProfileService>()
    val profile = profileService.profile
    if (!profile.isValid) return

    val sessionId = profile.sessionInfo?.sessionId
    val accountId = profile.account?.accountId
    if (sessionId.isNullOrEmpty() || accountId.isNullOrEmpty()) {
        return
    }
    val account = Account(sessionId = sessionId, accountId = accountId)
    val remoteAddress = "http://${DEFAULT_HOST}:${TCP_PORT}"
    innerInitDatabase(account)
    innerStartService(remoteAddress, account)
}

private fun innerInitDatabase(account: Account) {
    val initConfig = IMDatabaseInitConfig(
        account = account,
        factory = platformAppDatabaseFactory()
    )
    SingleIMManager.initDatabase(initConfig)
}

private fun innerStartService(remoteAddress: String, account: Account) {
    val initConfig = IMInitConfig(
        remoteAddress = remoteAddress,
        account = account
    )
    SingleIMManager.startService(initConfig)
}

fun stopChatService() {
    SingleIMManager.stopService()
    SingleIMManager.release()
}